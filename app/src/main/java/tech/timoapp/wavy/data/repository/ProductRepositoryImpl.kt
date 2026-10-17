package tech.timoapp.wavy.data.repository

import tech.timoapp.wavy.domain.model.ProductNotRemovableException
import tech.timoapp.wavy.domain.model.ServerException
import tech.timoapp.wavy.network.ApiService
import tech.timoapp.wavy.network.dto.ProductDetailsDto
import tech.timoapp.wavy.network.dto.ProductDto

class ProductRepositoryImpl(
    private val apiService: ApiService
) : ProductRepository {

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