package tech.timoapp.wavy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import tech.timoapp.wavy.ui.DetailsPageScreen
import tech.timoapp.wavy.ui.mockProduct

class ProductDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                DetailsPageScreen(
                    product = mockProduct,
                    onBackClick = { findNavController().popBackStack() })
            }
        }
    }
}