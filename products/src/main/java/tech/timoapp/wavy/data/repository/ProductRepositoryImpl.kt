package tech.timoapp.wavy.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tech.timoapp.wavy.data.paging.ProductPagingSource
import tech.timoapp.wavy.domain.model.Product
import tech.timoapp.wavy.domain.model.ProductNotRemovableException
import tech.timoapp.wavy.domain.model.ServerException
import tech.timoapp.wavy.domain.model.toProduct
import tech.timoapp.wavy.network.ApiService
import tech.timoapp.wavy.network.dto.ProductDetailsDto
import tech.timoapp.wavy.network.dto.ProductDto

class ProductRepositoryImpl(
    private val apiService: ApiService
) : ProductRepository {

    override fun getProductsPaged(): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                prefetchDistance = 2
            ),
            pagingSourceFactory = { ProductPagingSource(apiService) }
        ).flow.map { pagingData ->
            pagingData.map { it.toProduct() }
        }
    }

    override suspend fun getProducts(): Result<List<ProductDto>> {
        return try {
            val response = apiService.getProducts()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Result.success(body.data)
            } else {
                Result.failure(ServerException(response.code()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProductDetails(id: String): Result<ProductDetailsDto> {
        return try {
            val response = apiService.getProductDetails(id)
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Result.success(body)
            } else {
                Result.failure(ServerException(response.code()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addProduct(): Result<ProductDto> {
        return try {
            val response = apiService.generateProduct()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Result.success(body)
            } else {
                Result.failure(ServerException(response.code()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteProduct(id: String): Result<Unit> {
        return try {
            val response = apiService.deleteProduct(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorMsg = response.errorBody()?.string()?.trim()
                val exception = if (!errorMsg.isNullOrEmpty()) {
                    if (errorMsg.contains("product is not removable")) {
                        ProductNotRemovableException()
                    } else {
                        Exception(errorMsg)
                    }
                } else {
                    ServerException(response.code())
                }
                Result.failure(exception)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}