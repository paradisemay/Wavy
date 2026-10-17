package tech.timoapp.wavy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import tech.timoapp.wavy.products.R
import tech.timoapp.wavy.domain.model.Product
import tech.timoapp.wavy.domain.model.ProductNotRemovableException
import tech.timoapp.wavy.domain.usecase.AddProductUseCase
import tech.timoapp.wavy.domain.usecase.DeleteProductUseCase
import tech.timoapp.wavy.domain.usecase.GetProductsPagedUseCase
import tech.timoapp.wavy.model.ProductListUiEvent

class ProductListViewModel(
    private val getProductsPagedUseCase: GetProductsPagedUseCase,
    private val addProductUseCase: AddProductUseCase,
    private val deleteProductUseCase: DeleteProductUseCase
) : ViewModel() {

    val productsPagingFlow: Flow<PagingData<Product>> =
        getProductsPagedUseCase().cachedIn(viewModelScope)

    private val _eventFlow = MutableSharedFlow<ProductListUiEvent>()
    val eventFlow: SharedFlow<ProductListUiEvent> = _eventFlow.asSharedFlow()

    fun addProduct() {
        viewModelScope.launch {
            addProductUseCase().onSuccess {
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
                _eventFlow.emit(ProductListUiEvent.ProductDeleted)
            }.onFailure { error ->
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
