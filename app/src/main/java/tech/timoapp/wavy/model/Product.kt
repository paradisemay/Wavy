package tech.timoapp.wavy.model

import tech.timoapp.wavy.R
import tech.timoapp.wavy.network.dto.ProductDto

data class Product(
    val id: String,
    val article: String,
    val name: String,
    val price: String,
    val category: String,
    val imageRes: Int
)


fun ProductDto.toProduct(): Product = Product(
    id = this.id,
    article = this.article,
    name = this.name,
    price = "${this.price} ₽",
    category = this.category,
    imageRes = R.drawable.mock_product_image
)