package tech.timoapp.wavy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import tech.timoapp.wavy.data.local.AppDatabase
import tech.timoapp.wavy.data.repository.BalanceRepository
import tech.timoapp.wavy.model.ReviewsUiState
import tech.timoapp.wavy.model.toProductFeedbacksModel
import tech.timoapp.wavy.network.RetrofitClient
import tech.timoapp.wavy.ui.ReviewsScreen

class ReviewsFragment : Fragment() {

    private val uiState = mutableStateOf<ReviewsUiState>(ReviewsUiState.Loading)
    private val balanceRepository by lazy {
        val database = AppDatabase.getDatabase(requireContext())
        BalanceRepository(RetrofitClient.apiService, database.balanceDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ReviewsScreen(
                    state = uiState.value, onRetry = { loadReviewsData() })
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadReviewsData()
    }

    private fun loadReviewsData() {
        uiState.value = ReviewsUiState.Loading
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                coroutineScope {
                    val balanceDeferred = async { balanceRepository.getBalance() }
                    val feedbacksDeferred = async {
                        try {
                            val resp = RetrofitClient.apiService.getFeedbacks()
                            if (resp.isSuccessful && resp.body() != null) {
                                resp.body()!!.data.map { it.toProductFeedbacksModel() }
                            } else {
                                emptyList()
                            }
                        } catch (e: Exception) {
                            emptyList()
                        }
                    }
                    val balanceResult = balanceDeferred.await()
                    val productsModel = feedbacksDeferred.await()
                    uiState.value = ReviewsUiState.Success(
                        balance = balanceResult.balance,
                        products = productsModel,
                        isBalanceFromCache = balanceResult.isFromCache
                    )
                }
            } catch (e: Exception) {
                uiState.value = ReviewsUiState.Error(
                    "Ошибка сети: ${e.localizedMessage ?: "Неизвестная ошибка"}"
                )
            }
        }
    }
}
