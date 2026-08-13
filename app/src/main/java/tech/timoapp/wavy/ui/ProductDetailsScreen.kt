package tech.timoapp.wavy.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.timoapp.wavy.R
import tech.timoapp.wavy.model.ProductDetailsUiModel


@Composable
fun TitleAndPriceContent(product: ProductDetailsUiModel) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()
        ) {
            TitleText(
                text = product.price, modifier = Modifier.padding(end = 10.dp)
            )
            TitleSubText(product.article)
        }
        LargeText(product.title)
    }
}

@Composable
fun StatisticContent(product: ProductDetailsUiModel) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
            .background(
                color = colorResource(R.color.light_purple), shape = RoundedCornerShape(10.dp)
            )
            .padding(vertical = 10.dp, horizontal = 36.dp)
    ) {
        FactorIdBlock(
            largeText = product.rating,
            smallText = stringResource(R.string.rating),
            iconRes = R.drawable.ic_star
        )
        FactorIdBlock(
            largeText = product.ordersCount,
            smallText = stringResource(R.string.orders),
            iconRes = R.drawable.ic_box
        )
        FactorIdBlock(
            largeText = product.stockCount,
            smallText = stringResource(R.string.in_stock),
            iconRes = R.drawable.ic_cart_warehouse
        )

    }
}


@Composable
fun ImageContent(product: ProductDetailsUiModel) {
    Box(
        contentAlignment = Alignment.BottomStart,
    ) {
        RoundedImage(product.imageRes)
        ContentBadge(product)
    }
}


@Composable
fun ContentBadge(product: ProductDetailsUiModel) {
    Badge(
        modifier = Modifier.padding(10.dp)
    ) {
        MainText(product.category)
    }
}

@Composable
fun ProductDescription(product: ProductDetailsUiModel) {
    DescriptionContent {isExpanded ->
        MainText(
            text = product.description,
            maxLines = if (isExpanded) Int.MAX_VALUE else 7
        )
    }
}

@Composable
fun DetailsPageScreen(
    product: ProductDetailsUiModel,
    onBackClick: () -> Unit
) {

}

val mockProduct = ProductDetailsUiModel(
    imageRes = R.drawable.mock_product_image,
    category = "Одежда",
    price = "1 104 ₽",
    article = "435789542",
    title = "Блузка боди на запах без рукавов",
    rating = "4,5",
    ordersCount = "8 236",
    stockCount = "947",
    description = "Блузка-боди скимс без рукавов от Moremio – это идеальное сочетание стиля и комфорта, которое подчеркнет вашу женственность и красоту. Соблазнительный вырез на груди и обтягивающий крой создают сексуальный и элегантный образ, идеально подходящий как для вечерних летних выходов, так и для повседневной носки. Мягкая вискоза в составе ткани обеспечит комфорт в течение всего дня, а эластан придаст блузке-боди идеальную посадку по фигуре. Модель skims с запахом и стрингами добавит пикантности вашему образу, а складки на талии – изысканности. Благодаря эластичному материалу блузка-боди прекрасно подойдет для высоких девушек и женщин с пышными формами. Это универсальная модель, которая легко трансформируется из офисного варианта в вечерний наряд, просто добавив стильные аксессуары. Это та самая летняя блузка-боди из pinterest (пинтерест)"
)

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
fun TitleAndPriceContentPreview() {
    TitleAndPriceContent(
        product = mockProduct
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
fun StatisticContentPreview() {
    StatisticContent(
        product = mockProduct
    )
}


@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
fun ImageContentPreview() {
    ImageContent(
        product = mockProduct
    )
}


@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun ContentBadgePreview() {
    ContentBadge(
        product = mockProduct
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFEF7FF)
@Composable
fun ProductDescriptionPreview() {
    ProductDescription(
        product = mockProduct
    )
}

@Preview()
@Composable
fun DetailsPageScreenPreview() {
    DetailsPageScreen(
        product = mockProduct,
        onBackClick = {}
    )
}
