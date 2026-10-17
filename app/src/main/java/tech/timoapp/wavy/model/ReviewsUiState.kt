package tech.timoapp.wavy.model

sealed interface ReviewsUiState {
    data object Loading : ReviewsUiState
    data class Success(
        val balance: BalanceModel,
        val products: List<ProductFeedbacksModel>,
        val isBalanceFromCache: Boolean = false,
    ) : ReviewsUiState
    data class Error(val message: String) : ReviewsUiState
}
