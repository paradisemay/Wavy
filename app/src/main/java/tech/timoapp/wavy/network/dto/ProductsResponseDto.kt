package tech.timoapp.wavy.network.dto

import com.google.gson.annotations.SerializedName

data class ProductsResponseDto (
    @SerializedName("currentPage")
    val currentPage: Int,

    @SerializedName("totalPages")
    val totalPages: Int,

    @SerializedName("Data")
    val data: List<ProductDto>
)