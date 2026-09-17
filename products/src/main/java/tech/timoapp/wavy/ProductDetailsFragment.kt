package tech.timoapp.wavy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.timoapp.wavy.model.ProductDetailsUiState
import tech.timoapp.wavy.ui.DetailsPageScreen
import tech.timoapp.wavy.ui.DetailsPageSkeleton
import tech.timoapp.wavy.viewmodel.ProductDetailsViewModel

class ProductDetailsFragment : Fragment() {

    private val viewModel: ProductDetailsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val state by viewModel.uiState.collectAsState()
                when (val currentState = state) {
                    is ProductDetailsUiState.Success -> {
                        DetailsPageScreen(
                            product = currentState.product,
                            onBackClick = { findNavController().popBackStack() })
                    }
                    is ProductDetailsUiState.Error -> {
                        tech.timoapp.wavy.ui.ErrorStateScreen(
                            onRetry = {
                                val productId = arguments?.getString("PRODUCT_ID") ?: return@ErrorStateScreen
                                viewModel.loadProduct(productId)
                            },
                            onBackClick = { findNavController().popBackStack() }
                        )
                    }
                    is ProductDetailsUiState.Loading -> {
                        DetailsPageSkeleton(
                            onBackClick = { findNavController().popBackStack() })
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val productId = arguments?.getString("PRODUCT_ID") ?: return
        viewModel.loadProduct(productId)
    }
}
