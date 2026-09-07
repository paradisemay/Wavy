package tech.timoapp.wavy.model

import tech.timoapp.wavy.network.dto.ProductDto

data class Product(
    val id: String,
    val article: String,
    val name: String,
    val price: String,
    val category: String,
    val imageUrl: String
)


fun ProductDto.toProduct(): Product = Product(
    id = this.id,
    article = this.article,
    name = this.name,
    price = "${this.price} ₽",
    category = this.category,
    imageUrl = if (this.imageUrl.startsWith("http")) this.imageUrl else "https://${this.imageUrl}"
)