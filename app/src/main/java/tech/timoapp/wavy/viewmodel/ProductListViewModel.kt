package tech.timoapp.wavy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import tech.timoapp.wavy.data.repository.ProductRepository
import tech.timoapp.wavy.model.ProductListUiState

class ProductListViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductListUiState>(ProductListUiState.Loading)
    val uiState: StateFlow<ProductListUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    fun loadProducts() {
        _uiState.value = ProductListUiState.Loading
        viewModelScope.launch {
            repository.getProducts().onSuccess { products ->
                _uiState.value = ProductListUiState.Success(products)
            }.onFailure { error ->
                _uiState.value =
                    ProductListUiState.Error(error.localizedMessage ?: "Ошибка загрузки")
            }
        }
    }
}
