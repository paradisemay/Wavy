package tech.timoapp.wavy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import tech.timoapp.wavy.model.ProductDetailsModel
import tech.timoapp.wavy.model.toDetailsModel
import tech.timoapp.wavy.network.RetrofitClient
import tech.timoapp.wavy.ui.DetailsPageScreen
import tech.timoapp.wavy.util.safeApiCall

class ProductDetailsFragment : Fragment() {

    private val productState = mutableStateOf<ProductDetailsModel?>(null)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val product = productState.value
                if (product != null) {
                    DetailsPageScreen(
                        product = product,
                        onBackClick = { findNavController().popBackStack() })
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val productId = arguments?.getString("PRODUCT_ID") ?: return
        loadSelectedProduct(productId)
    }

    private fun loadSelectedProduct(productId: String) {
        safeApiCall(
            apiCall = { RetrofitClient.apiService.getProductDetails(productId) },
            onSuccess = { dto ->
                productState.value = dto.toDetailsModel()
            }
        )
    }
}