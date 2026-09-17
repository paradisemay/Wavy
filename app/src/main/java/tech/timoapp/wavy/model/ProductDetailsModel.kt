package tech.timoapp.wavy.model

import tech.timoapp.wavy.R
import tech.timoapp.wavy.network.dto.ProductDetailsDto
import tech.timoapp.wavy.util.formatPrice
import tech.timoapp.wavy.util.formatRating

data class ProductDetailsModel(
    val imageUrl: String,
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
        imageUrl = if (this.imageUrl.startsWith("http")) this.imageUrl else "https://${this.imageUrl}",
        category = this.category,
        price = this.price.formatPrice(),
        article = this.article,
        title = this.name,
        rating = this.rating.formatRating(),
        ordersCount = this.ordersCount.toString(),
        stockCount = this.stockCount.toString(),
        description = this.description
    )
}
