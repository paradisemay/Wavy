package tech.timoapp.wavy.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import tech.timoapp.wavy.network.dto.BalanceInfoDto
import tech.timoapp.wavy.network.dto.FeedbacksResponseDto
import tech.timoapp.wavy.network.dto.ProductDetailsDto
import tech.timoapp.wavy.network.dto.ProductsResponseDto

interface ApiService {
    @GET("api/products")
    suspend fun getProducts(): Response<ProductsResponseDto>

    @GET("api/products/{id}")
    suspend fun getProductDetails(@Path("id") id: String): Response<ProductDetailsDto>

    @GET("api/balanceInfo")
    suspend fun getBalanceInfo(): Response<BalanceInfoDto>

    @GET("api/feedbacks")
    suspend fun getFeedbacks(@Query("page") page: Int? = null): Response<FeedbacksResponseDto>
}