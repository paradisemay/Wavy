package tech.timoapp.wavy.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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