package tech.timoapp.wavy.model

data class ProductDetailsUiModel(
    val imageRes: Int,
    val category: String,
    val price: String,
    val article: String,
    val title: String,
    val rating: String,
    val ordersCount: String,
    val stockCount: String,
    val description: String
)