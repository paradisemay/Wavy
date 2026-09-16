package tech.timoapp.wavy.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import tech.timoapp.wavy.R
import tech.timoapp.wavy.model.FeedbackModel
import tech.timoapp.wavy.model.ProductFeedbacksModel
import tech.timoapp.wavy.model.RatingDistributionModel

@Composable
fun CommentCard(
    feedback: FeedbackModel,
    modifier: Modifier = Modifier,
    isExpanded: Boolean = false,
    onClick: (() -> Unit)? = null,
    backgroundColor: Color = colorResource(R.color.light_purple_10),
) {
    val contentPadding = if (backgroundColor == Color.Transparent) 0.dp else 10.dp
    val baseModifier = modifier
        .then(
            if (backgroundColor != Color.Transparent) {
                Modifier.background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(16.dp)
                )
            } else Modifier
        )
        .then(
            if (onClick != null) {
                Modifier.clickable(onClick = onClick)
            } else Modifier
        )
        .padding(contentPadding)

    Column(modifier = baseModifier) {
        if (feedback.isRefund) {
            Text(
                text = "ВОЗВРАТ",
                color = colorResource(R.color.red_text_color),
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            ReviewStarsText(feedback.ratingFormatted)
            MetricBadgeText(feedback.buyerName)
        }

        if (feedback.photos.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            if (isExpanded) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    feedback.photos.chunked(4).forEach { photoRow ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            photoRow.forEach { photoUrl ->
                                AsyncImage(
                                    model = photoUrl,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(72.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
            } else {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    feedback.photos.take(6).forEach { photoUrl ->
                        AsyncImage(
                            model = photoUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }

        if (feedback.pros != null) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.main_text_color)
                        )
                    ) {
                        append("Достоинства: ")
                    }
                    withStyle(SpanStyle(color = colorResource(R.color.main_text_color))) {
                        append(feedback.pros)
                    }
                }, fontSize = 14.sp, lineHeight = 20.sp
            )
        }

        if (feedback.cons != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.main_text_color)
                        )
                    ) {
                        append("Недостатки: ")
                    }
                    withStyle(SpanStyle(color = colorResource(R.color.main_text_color))) {
                        append(feedback.cons)
                    }
                }, fontSize = 14.sp, lineHeight = 20.sp
            )
        }

        if (feedback.comment != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = feedback.comment,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight(400),
                color = colorResource(R.color.main_text_color),
                maxLines = if (isExpanded) Int.MAX_VALUE else 4,
                overflow = if (isExpanded) TextOverflow.Clip else TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun RatingDistributionBar(
    product: ProductFeedbacksModel,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .height(52.dp)
                .background(
                    color = colorResource(R.color.light_purple),
                    shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
                )
                .padding(start = 8.dp, end = 8.dp, top = 6.dp, bottom = 6.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = product.ratingFormatted,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                lineHeight = 14.sp,
                color = colorResource(R.color.dark_purple),
                maxLines = 1
            )
            Text(
                text = product.totalFeedbacksCountFormatted,
                fontWeight = FontWeight.Normal,
                fontSize = 11.sp,
                lineHeight = 14.sp,
                color = colorResource(R.color.dark_purple),
                maxLines = 1
            )
        }

        RatingCell(star = "5", count = product.distribution.five)
        RatingCell(star = "4", count = product.distribution.four)
        RatingCell(star = "3", count = product.distribution.three)
        RatingCell(star = "2", count = product.distribution.two)
        RatingCell(
            star = "1",
            count = product.distribution.one,
            shape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)
        )
    }
}

@Composable
private fun RatingCell(
    star: String,
    count: Int,
    shape: Shape = RoundedCornerShape(0.dp),
    modifier: Modifier = Modifier
) {
    val textColor = if (count > 0) {
        colorResource(R.color.main_text_color)
    } else {
        Color(0xFFC7C5D0)
    }

    Column(
        modifier = modifier
            .width(48.dp)
            .height(52.dp)
            .background(
                color = Color(0xFF6750A4).copy(alpha = 0.10f),
                shape = shape
            )
            .padding(start = 8.dp, end = 8.dp, top = 6.dp, bottom = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = star,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 14.sp,
            color = textColor
        )
        Text(
            text = count.toString(),
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 14.sp,
            color = textColor
        )
    }
}

@Composable
fun ProductHeader(
    product: ProductFeedbacksModel,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = product.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = product.name,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = colorResource(R.color.main_text_color),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = product.ordersCountFormatted,
                    fontSize = 12.sp,
                    color = colorResource(R.color.sub_text_color)
                )
                Text(
                    text = product.refundsPercentFormatted,
                    fontSize = 12.sp,
                    color = colorResource(R.color.sub_text_color)
                )
            }
        }
    }
}

@Composable
fun ReviewBlock(
    product: ProductFeedbacksModel,
    onFeedbackClick: (FeedbackModel) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        ProductHeader(product = product)

        Spacer(modifier = Modifier.height(10.dp))

        RatingDistributionBar(product = product)

        if (product.feedbacks.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(product.feedbacks) { feedback ->
                    CommentCard(
                        feedback = feedback,
                        isExpanded = false,
                        onClick = { onFeedbackClick(feedback) },
                        modifier = Modifier
                            .fillParentMaxWidth(0.85f)
                            .height(276.dp)
                    )
                }
            }
        }
    }
}

val mockFeedback1 = FeedbackModel(
    id = "1",
    buyerName = "Александра",
    rating = 5,
    ratingFormatted = "5★",
    pros = "Очень красивая футболка!",
    cons = "немного жмет, но думаю что разносится",
    comment = "Лонгслив хороший, лиф был надет не под этот лонг конечно, поэтому немного не так грудь смотрится. Но в целом отличный лонгслив, мерила другие модели этого бренда и это просто ВАУ. Нашла себе лонгслив на празднования Нового года,цена вообще супер. Рекомендую к покупке!",
    photos = listOf(
        "https://basket-17.wbbasket.ru/vol2679/part267903/267903164/images/big/1.webp",
        "https://basket-17.wbbasket.ru/vol2679/part267903/267903164/images/big/2.webp",
        "https://basket-17.wbbasket.ru/vol2679/part267903/267903164/images/big/3.webp",
        "https://basket-17.wbbasket.ru/vol2679/part267903/267903164/images/big/4.webp",
        "https://basket-17.wbbasket.ru/vol2679/part267903/267903164/images/big/1.webp",
        "https://basket-17.wbbasket.ru/vol2679/part267903/267903164/images/big/2.webp",
        "https://basket-17.wbbasket.ru/vol2679/part267903/267903164/images/big/3.webp"
    ),
    isRefund = false
)

val mockFeedback2 = FeedbackModel(
    id = "2",
    buyerName = "Михаил",
    rating = 1,
    ratingFormatted = "1★",
    pros = "цвет на фотках красивый",
    cons = "а мне вот приехал некрасивый!!",
    comment = "хотел сделать жене приятный подарок, заказывал в зеленом варианте — ее любимый цвет. а по итогу привезли какой-то цвет грязи и лужи. возвращаю!",
    photos = listOf(
        "https://basket-17.wbbasket.ru/vol2679/part267903/267903164/images/big/1.webp"
    ),
    isRefund = true
)

val mockProductFeedback = ProductFeedbacksModel(
    id = "1",
    name = "Блузка боди на запах без рукавов",
    imageUrl = "https://basket-17.wbbasket.ru/vol2679/part267903/267903164/images/big/1.webp",
    ordersCountFormatted = "8 236 заказов",
    refundsPercentFormatted = "12,42% возвратов",
    ratingFormatted = "4,89 ★",
    totalFeedbacksCountFormatted = "504 отзыва",
    distribution = RatingDistributionModel(
        five = 413, four = 56, three = 14, two = 0, one = 7
    ),
    feedbacks = listOf(mockFeedback1, mockFeedback2)
)

@Preview(showBackground = true)
@Composable
fun CommentCardCompactPreview() {
    CommentCard(
        feedback = mockFeedback1,
        isExpanded = false,
        modifier = Modifier
            .padding(16.dp)
            .width(340.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun CommentCardExpandedPreview() {
    CommentCard(
        feedback = mockFeedback1,
        isExpanded = true,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
fun ReviewBlockPreview() {
    ReviewBlock(
        product = mockProductFeedback, modifier = Modifier.padding(16.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun RatingDistributionBarPreview() {
    RatingDistributionBar(
        product = mockProductFeedback,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun CacheNotificationBanner(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = colorResource(R.color.light_purple).copy(alpha = 0.5f),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Данные получены из кеша. Нажмите для обновления",
            fontSize = 12.sp,
            color = colorResource(R.color.dark_purple),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "↻",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.dark_purple)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CacheNotificationBannerPreview() {
    CacheNotificationBanner(
        onClick = {},
        modifier = Modifier.padding(16.dp)
    )
}
