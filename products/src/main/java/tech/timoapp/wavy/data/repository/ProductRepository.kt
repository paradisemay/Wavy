package tech.timoapp.wavy.data.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import tech.timoapp.wavy.domain.model.Product
import tech.timoapp.wavy.network.dto.ProductDetailsDto
import tech.timoapp.wavy.network.dto.ProductDto

interface ProductRepository {
    suspend fun getProducts(): Result<List<ProductDto>>
    fun getProductsPaged(): Flow<PagingData<Product>>
    suspend fun getProductDetails(id: String): Result<ProductDetailsDto>
    suspend fun addProduct(): Result<ProductDto>
    suspend fun deleteProduct(id: String): Result<Unit>
}
