package tech.timoapp.wavy.data.repository

import tech.timoapp.wavy.model.Product
import tech.timoapp.wavy.model.ProductDetailsState

interface ProductRepository {
    suspend fun getProducts(): Result<List<Product>>
    suspend fun getProductDetails(id: String): Result<ProductDetailsState>
}
