package tech.timoapp.wavy.model

sealed interface ProductListUiEvent {
    object ProductAdded : ProductListUiEvent
    object ProductDeleted : ProductListUiEvent
    data class Error(val message: String? = null, val messageRes: Int? = null) : ProductListUiEvent
}
