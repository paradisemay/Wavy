package tech.timoapp.wavy.network.dto

import com.google.gson.annotations.SerializedName

data class ProductDto (
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("article")
    val article: String,

    @SerializedName("category")
    val category: String,

    @SerializedName("imageUrl")
    val imageUrl: String,

    @SerializedName("isRemovable")
    val isRemovable: Boolean,

    @SerializedName("oldPrice")
    val oldPrice: Double?,

    @SerializedName("price")
    val price: Double
)
