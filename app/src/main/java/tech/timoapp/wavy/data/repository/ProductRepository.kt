package tech.timoapp.wavy.data.repository

import tech.timoapp.wavy.network.dto.ProductDetailsDto
import tech.timoapp.wavy.network.dto.ProductDto

interface ProductRepository {
    suspend fun getProducts(): Result<List<ProductDto>>
    suspend fun getProductDetails(id: String): Result<ProductDetailsDto>
    suspend fun addProduct(): Result<ProductDto>
    suspend fun deleteProduct(id: String): Result<Unit>
}
