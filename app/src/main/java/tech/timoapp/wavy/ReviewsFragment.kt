package tech.timoapp.wavy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.timoapp.wavy.ui.ReviewsScreen
import tech.timoapp.wavy.viewmodel.ReviewsViewModel

class ReviewsFragment : Fragment() {
    private val viewModel: ReviewsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ReviewsScreen(
                    state = viewModel.uiState.value,
                    onRetry = { viewModel.loadReviewsData() }
                )
            }
        }
    }
}
