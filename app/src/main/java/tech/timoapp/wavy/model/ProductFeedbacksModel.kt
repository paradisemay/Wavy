package tech.timoapp.wavy.model

import tech.timoapp.wavy.network.dto.FeedbackItemDto
import tech.timoapp.wavy.network.dto.ProductFeedbackDto
import tech.timoapp.wavy.util.formatRating
import java.util.Locale

data class ProductFeedbacksModel(
    val id: String,
    val name: String,
    val imageUrl: String,
    val ordersCountFormatted: String,
    val refundsPercentFormatted: String,
    val ratingFormatted: String,
    val totalFeedbacksCountFormatted: String,
    val distribution: RatingDistributionModel,
    val feedbacks: List<FeedbackModel>
)

data class RatingDistributionModel(
    val five: Int,
    val four: Int,
    val three: Int,
    val two: Int,
    val one: Int
)

data class FeedbackModel(
    val id: String,
    val buyerName: String,
    val rating: Int,
    val ratingFormatted: String = "$rating★",
    val pros: String?,
    val cons: String?,
    val comment: String?,
    val photos: List<String>,
    val isRefund: Boolean
)

fun ProductFeedbackDto.toProductFeedbacksModel(): ProductFeedbacksModel {
    val imgUrl = imageUrl
    val normalizedProductImage = if (imgUrl.isNullOrBlank()) ""
    else if (imgUrl.startsWith("http")) imgUrl else "https://$imgUrl"

    val orders = ordersCount?.toString()?.filter { it.isDigit() }?.toIntOrNull() ?: 0
    val ordersFormatted = "${formatCount(orders)} ${getPluralOrders(orders)}"

    val refundsFormatted = String.format(Locale.US, "%.2f%%", refundsPercent)
        .replace(".00", "")
        .replace('.', ',') + " возвратов"

    val count5 = feedbacks.count { it.rating == 5 }
    val count4 = feedbacks.count { it.rating == 4 }
    val count3 = feedbacks.count { it.rating == 3 }
    val count2 = feedbacks.count { it.rating == 2 }
    val count1 = feedbacks.count { it.rating == 1 }
    val totalCount = feedbacks.size

    return ProductFeedbacksModel(
        id = id,
        name = name,
        imageUrl = normalizedProductImage,
        ordersCountFormatted = ordersFormatted,
        refundsPercentFormatted = refundsFormatted,
        ratingFormatted = "${rating.formatRating()} ★",
        totalFeedbacksCountFormatted = "$totalCount ${getPluralReviews(totalCount)}",
        distribution = RatingDistributionModel(
            five = count5,
            four = count4,
            three = count3,
            two = count2,
            one = count1
        ),
        feedbacks = feedbacks.map { it.toFeedbackModel() }
    )
}

fun FeedbackItemDto.toFeedbackModel(): FeedbackModel {
    val normalizedPhotos = (photosUrl ?: emptyList()).map { url ->
        if (url.startsWith("http")) url else "https://$url"
    }

    return FeedbackModel(
        id = id,
        buyerName = buyerName,
        rating = rating,
        ratingFormatted = "$rating★",
        pros = pros?.takeIf { it.isNotBlank() },
        cons = cons?.takeIf { it.isNotBlank() },
        comment = comment?.takeIf { it.isNotBlank() },
        photos = normalizedPhotos,
        isRefund = isRefund
    )
}

private fun formatCount(count: Int): String {
    return String.format(Locale.US, "%,d", count).replace(',', ' ')
}

private fun getPluralOrders(count: Int): String {
    val mod100 = count % 100
    val mod10 = count % 10
    return when {
        mod100 in 11..19 -> "заказов"
        mod10 == 1 -> "заказ"
        mod10 in 2..4 -> "заказа"
        else -> "заказов"
    }
}

private fun getPluralReviews(count: Int): String {
    val mod100 = count % 100
    val mod10 = count % 10
    return when {
        mod100 in 11..19 -> "отзывов"
        mod10 == 1 -> "отзыв"
        mod10 in 2..4 -> "отзыва"
        else -> "отзывов"
    }
}
