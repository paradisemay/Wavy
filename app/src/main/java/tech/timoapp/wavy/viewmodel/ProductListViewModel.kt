package tech.timoapp.wavy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import tech.timoapp.wavy.R
import tech.timoapp.wavy.domain.model.ProductNotRemovableException
import tech.timoapp.wavy.domain.usecase.AddProductUseCase
import tech.timoapp.wavy.domain.usecase.DeleteProductUseCase
import tech.timoapp.wavy.domain.usecase.GetProductsUseCase
import tech.timoapp.wavy.model.ProductListUiEvent
import tech.timoapp.wavy.model.ProductListUiState

class ProductListViewModel(
    private val getProductsUseCase: GetProductsUseCase,
    private val addProductUseCase: AddProductUseCase,
    private val deleteProductUseCase: DeleteProductUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductListUiState>(ProductListUiState.Loading)
    val uiState: StateFlow<ProductListUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<ProductListUiEvent>()
    val eventFlow: SharedFlow<ProductListUiEvent> = _eventFlow.asSharedFlow()

    init {
        loadProducts()
    }

    fun loadProducts() {
        _uiState.value = ProductListUiState.Loading
        viewModelScope.launch {
            getProductsUseCase().onSuccess { products ->
                if (products.isEmpty()) {
                    _uiState.value = ProductListUiState.Empty
                } else {
                    _uiState.value = ProductListUiState.Success(products)
                }
            }.onFailure { error ->
                _uiState.value =
                    ProductListUiState.Error(error.localizedMessage ?: "")
            }
        }
    }

    fun addProduct() {
        viewModelScope.launch {
            addProductUseCase().onSuccess { newProduct ->
                val currentProducts =
                    (_uiState.value as? ProductListUiState.Success)?.products ?: emptyList()
                _uiState.value = ProductListUiState.Success(listOf(newProduct) + currentProducts)
                _eventFlow.emit(ProductListUiEvent.ProductAdded)
            }.onFailure { error ->
                _eventFlow.emit(
                    ProductListUiEvent.Error(
                        message = error.localizedMessage,
                        messageRes = R.string.error_add_product
                    )
                )
            }
        }
    }

    fun deleteProduct(id: String) {
        viewModelScope.launch {
            deleteProductUseCase(id).onSuccess {
                val currentProducts =
                    (_uiState.value as? ProductListUiState.Success)?.products ?: emptyList()
                val updatedProducts = currentProducts.filter { it.id != id }
                if (updatedProducts.isEmpty()) {
                    _uiState.value = ProductListUiState.Empty
                } else {
                    _uiState.value = ProductListUiState.Success(updatedProducts)
                }
                _eventFlow.emit(ProductListUiEvent.ProductDeleted)
            }.onFailure { error ->
                val currentProducts =
                    (_uiState.value as? ProductListUiState.Success)?.products ?: emptyList()
                val updatedProducts = currentProducts.map {
                    if (it.id == id) it.copy(isRemovable = false) else it
                }
                _uiState.value = ProductListUiState.Success(updatedProducts)
                val event = if (error is ProductNotRemovableException) {
                    ProductListUiEvent.Error(messageRes = R.string.error_product_not_removable)
                } else {
                    ProductListUiEvent.Error(
                        message = error.localizedMessage,
                        messageRes = R.string.error_delete_product
                    )
                }
                _eventFlow.emit(event)
            }
        }
    }
}
