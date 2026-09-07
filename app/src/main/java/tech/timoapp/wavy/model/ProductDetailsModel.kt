package tech.timoapp.wavy.model

import tech.timoapp.wavy.R
import tech.timoapp.wavy.network.dto.ProductDetailsDto

data class ProductDetailsModel(
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

fun ProductDetailsDto.toDetailsModel(): ProductDetailsModel {
    return ProductDetailsModel(
        imageRes = R.drawable.mock_product_image,
        category = this.category,
        price = "${this.price} ₽",
        article = this.article,
        title = this.name,
        rating = this.rating.toString(),
        ordersCount = this.ordersCount.toString(),
        stockCount = this.stockCount.toString(),
        description = this.description
    )
}
