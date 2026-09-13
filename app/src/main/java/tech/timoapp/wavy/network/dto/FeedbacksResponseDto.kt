package tech.timoapp.wavy.network.dto

import com.google.gson.annotations.SerializedName

data class FeedbacksResponseDto(
    @SerializedName("currentPage")
    val currentPage: Int,

    @SerializedName("totalPages")
    val totalPages: Int,

    @SerializedName("Data")
    val data: List<ProductFeedbackDto>
)

data class ProductFeedbackDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("imageUrl")
    val imageUrl: String?,

    @SerializedName("rating")
    val rating: Double,

    @SerializedName("ordersCount")
    val ordersCount: Any?,

    @SerializedName("refundsPercent")
    val refundsPercent: Double,

    @SerializedName("feedbacks")
    val feedbacks: List<FeedbackItemDto>
)

data class FeedbackItemDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("buyerName")
    val buyerName: String,

    @SerializedName("rating")
    val rating: Int,

    @SerializedName("pros")
    val pros: String?,

    @SerializedName("cons")
    val cons: String?,

    @SerializedName("comment")
    val comment: String?,

    @SerializedName("photosURL")
    val photosUrl: List<String>?,

    @SerializedName("isRefund")
    val isRefund: Boolean
)
