package tech.timoapp.wavy.data.repository

import tech.timoapp.wavy.model.Product
import tech.timoapp.wavy.model.ProductDetailsState
import tech.timoapp.wavy.model.toDetailsModel
import tech.timoapp.wavy.model.toProduct
import tech.timoapp.wavy.network.ApiService

class ProductRepositoryImpl(
    private val apiService: ApiService
) : ProductRepository {

    override suspend fun getProducts(): Result<List<Product>> {
        return try {
            val response = apiService.getProducts()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.data.map { it.toProduct() })
            } else {
                Result.failure(Exception("Ошибка сервера: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProductDetails(id: String): Result<ProductDetailsState> {
        return try {
            val response = apiService.getProductDetails(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toDetailsModel())
            } else {
                Result.failure(Exception("Ошибка сервера: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}