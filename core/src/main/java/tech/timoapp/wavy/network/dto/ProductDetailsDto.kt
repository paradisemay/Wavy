package tech.timoapp.wavy.network.dto

import com.google.gson.annotations.SerializedName

data class ProductDetailsDto (
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("article")
    val article: String,

    @SerializedName("category")
    val category: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("imageUrl")
    val imageUrl: String,

    @SerializedName("oldPrice")
    val oldPrice: Double?,

    @SerializedName("price")
    val price: Double,

    @SerializedName("rating")
    val rating: Double,

    @SerializedName("warehouseQuantity")
    val stockCount: Int,

    @SerializedName("ordersCount")
    val ordersCount: Int
)
