package tech.timoapp.wavy.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            TitleText(
                text = product.price,
                modifier = Modifier.padding(end = 10.dp)
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
            .padding(top=16.dp)
            .fillMaxWidth()
            .background(
                color = colorResource(R.color.light_purple),
                shape = RoundedCornerShape(10.dp)
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
fun FactorIdBlock(largeText: String, smallText: String, iconRes: Int) {
    Column {
        Row {
            LargeStatisticText(
                text = largeText,
                modifier = Modifier.padding(end = 4.dp)
            )
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterVertically).size(26.21.dp)
            )
        }
        MainStatisticText(smallText)
    }
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
    description = "Блузка-боди скимс без рукавов от Moremio – это идеальное сочетание стиля и комфорта, которое подчеркнет вашу женственность и красоту. Соблазнительный вырез на груди и обтягивающий крой создают сексуальный и элегантный образ, идеально подходящий как для вечерних летних выходов, так и для\u2028повседневной носки. Мягкая вискоза в составе ткани обеспечит комфорт в течение всего дня, а эластан придаст блузке-боди идеальную посадку по фигуре. Модель skims с запахом и стрингами добавит пикантности вашему образу, а складки на талии – изысканности. Благодаря эластичному материалу блузка-боди прекрасно подойдет для высоких девушек и женщин с пышными формами. Это универсальная модель, которая легко трансформируется из офисного варианта в вечерний наряд, просто добавив стильные аксессуары. Это та самая летняя блузка-боди из pinterest (пинтерест)"
)

@Preview(showBackground = true)
@Composable
fun TitleAndPriceContentPreview() {
    TitleAndPriceContent(
        product = mockProduct
    )
}

@Preview(showBackground = true)
@Composable
fun StatisticContentPreview() {
    StatisticContent(
        product = mockProduct
    )
}

@Preview(showBackground = true)
@Composable
fun FactorIdBlockPreview() {
    FactorIdBlock(
        largeText = mockProduct.rating,
        smallText = stringResource(R.string.rating),
        iconRes = R.drawable.ic_star
    )
}