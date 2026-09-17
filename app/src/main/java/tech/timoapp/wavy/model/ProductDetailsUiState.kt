package tech.timoapp.wavy.model

sealed interface ProductDetailsUiState {
    data object Loading : ProductDetailsUiState
    data class Success(val product: ProductDetailsState) : ProductDetailsUiState
    data class Error(val message: String) : ProductDetailsUiState
}
