package tech.timoapp.wavy.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import tech.timoapp.wavy.data.repository.BalanceRepository
import tech.timoapp.wavy.model.ReviewsUiState
import tech.timoapp.wavy.model.toProductFeedbacksModel
import tech.timoapp.wavy.network.ApiService
import kotlin.collections.emptyList

class ReviewsViewModel(
    private val balanceRepository: BalanceRepository, private val apiService: ApiService
) : ViewModel() {

    val uiState = mutableStateOf<ReviewsUiState>(ReviewsUiState.Loading)

    init {
        loadReviewsData()
    }

    fun loadReviewsData() {
        uiState.value = ReviewsUiState.Loading
        viewModelScope.launch {
            try {
                coroutineScope {
                    val balanceDeferred = async { balanceRepository.getBalance() }
                    val feedbacksDeferred = async {
                        try {
                            val resp = apiService.getFeedbacks()
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
