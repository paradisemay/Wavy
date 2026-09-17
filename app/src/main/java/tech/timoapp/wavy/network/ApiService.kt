package tech.timoapp.wavy.network

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import tech.timoapp.wavy.network.dto.BalanceInfoDto
import tech.timoapp.wavy.network.dto.FeedbacksResponseDto
import tech.timoapp.wavy.network.dto.ProductDetailsDto
import tech.timoapp.wavy.network.dto.ProductDto
import tech.timoapp.wavy.network.dto.ProductsResponseDto

interface ApiService {
    @GET("api/products")
    suspend fun getProducts(): Response<ProductsResponseDto>

    @POST("api/products/generate")
    suspend fun generateProduct(): Response<ProductDto>

    @GET("api/products/{id}")
    suspend fun getProductDetails(@Path("id") id: String): Response<ProductDetailsDto>

    @DELETE("api/products/{id}")
    suspend fun deleteProduct(@Path("id") id: String): Response<Unit>

    @GET("api/balanceInfo")
    suspend fun getBalanceInfo(): Response<BalanceInfoDto>

    @GET("api/feedbacks")
    suspend fun getFeedbacks(@Query("page") page: Int? = null): Response<FeedbacksResponseDto>
}