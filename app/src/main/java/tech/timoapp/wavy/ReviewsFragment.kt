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
import tech.timoapp.wavy.model.ReviewsUiState
import tech.timoapp.wavy.model.toBalanceModel
import tech.timoapp.wavy.model.toProductFeedbacksModel
import tech.timoapp.wavy.network.RetrofitClient
import tech.timoapp.wavy.ui.ReviewsScreen

class ReviewsFragment : Fragment() {

    private val uiState = mutableStateOf<ReviewsUiState>(ReviewsUiState.Loading)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ReviewsScreen(
                    state = uiState.value,
                    onRetry = { loadReviewsData() }
                )
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
                    val balanceDeferred = async { RetrofitClient.apiService.getBalanceInfo() }
                    val feedbacksDeferred = async { RetrofitClient.apiService.getFeedbacks() }

                    val balanceResponse = balanceDeferred.await()
                    val feedbacksResponse = feedbacksDeferred.await()

                    if (balanceResponse.isSuccessful && feedbacksResponse.isSuccessful) {
                        val balanceBody = balanceResponse.body()
                        val feedbacksBody = feedbacksResponse.body()

                        if (balanceBody != null && feedbacksBody != null) {
                            uiState.value = ReviewsUiState.Success(
                                balance = balanceBody.toBalanceModel(),
                                products = feedbacksBody.data.map { it.toProductFeedbacksModel() }
                            )
                        } else {
                            uiState.value = ReviewsUiState.Error("Пустой ответ от сервера")
                        }
                    } else {
                        val errorCode = if (!balanceResponse.isSuccessful) {
                            balanceResponse.code()
                        } else {
                            feedbacksResponse.code()
                        }
                        uiState.value = ReviewsUiState.Error("Ошибка сервера: $errorCode")
                    }
                }
            } catch (e: Exception) {
                uiState.value = ReviewsUiState.Error(
                    "Ошибка сети: ${e.localizedMessage ?: "Неизвестная ошибка"}"
                )
            }
        }
    }
}
