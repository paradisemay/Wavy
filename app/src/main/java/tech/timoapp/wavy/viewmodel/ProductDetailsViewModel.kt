package tech.timoapp.wavy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import tech.timoapp.wavy.data.repository.ProductRepository
import tech.timoapp.wavy.model.ProductDetailsUiState

class ProductDetailsViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductDetailsUiState>(ProductDetailsUiState.Loading)
    val uiState: StateFlow<ProductDetailsUiState> = _uiState.asStateFlow()

    fun loadProduct(productId: String) {
        _uiState.value = ProductDetailsUiState.Loading
        viewModelScope.launch {
            repository.getProductDetails(productId).onSuccess { product ->
                _uiState.value = ProductDetailsUiState.Success(product)
            }.onFailure { error ->
                _uiState.value =
                    ProductDetailsUiState.Error(error.localizedMessage ?: "Ошибка загрузки")
            }
        }
    }
}
