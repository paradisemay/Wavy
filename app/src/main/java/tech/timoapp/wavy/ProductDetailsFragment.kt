package tech.timoapp.wavy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import tech.timoapp.wavy.model.ProductDetailsModel
import tech.timoapp.wavy.model.toDetailsModel
import tech.timoapp.wavy.ui.DetailsPageScreen

class ProductDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val productId = arguments?.getString("PRODUCT_ID")
        val selectedProduct = ProductListFragment.getMockProducts().find { it.article == productId }
        val uiModel = selectedProduct.toDetailsModel()

        return ComposeView(requireContext()).apply {
            setContent {
                DetailsPageScreen(
                    product = uiModel,
                    onBackClick = { findNavController().popBackStack() })
            }
        }
    }
}