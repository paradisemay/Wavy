package tech.timoapp.wavy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.timoapp.wavy.reviews.R
import tech.timoapp.wavy.model.ReviewsUiState
import tech.timoapp.wavy.ui.ReviewsScreen
import tech.timoapp.wavy.viewmodel.ReviewsViewModel

class ReviewsFragment : Fragment() {
    private val viewModel: ReviewsViewModel by viewModel()
    private var scrollToTopTrigger by mutableIntStateOf(0)

    fun scrollToTop() {
        scrollToTopTrigger++
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val composeView = ComposeView(requireContext()).apply {
            setContent {
                ReviewsScreen(
                    state = viewModel.uiState.value,
                    onRetry = { viewModel.loadReviewsData() },
                    scrollToTopTrigger = scrollToTopTrigger
                )
            }
        }

        return SwipeRefreshLayout(requireContext()).apply {
            setColorSchemeResources(R.color.dark_purple)
            addView(composeView)
            setOnRefreshListener {
                viewModel.loadReviewsData()
            }
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    snapshotFlow { viewModel.uiState.value }.collect { state ->
                        if (state !is ReviewsUiState.Loading) {
                            isRefreshing = false
                        }
                    }
                }
            }
        }
    }
}
