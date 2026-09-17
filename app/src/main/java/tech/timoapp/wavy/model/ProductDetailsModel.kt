package tech.timoapp.wavy.model

import tech.timoapp.wavy.R

data class ProductDetailsModel(
    val imageRes: Int,
    val category: String,
    val price: String,
    val article: String,
    val title: String,
    val rating: String,
    val ordersCount: String,
    val stockCount: String,
    val description: String
)

fun Product?.toDetailsModel(): ProductDetailsModel {
    return ProductDetailsModel(
        imageRes = this?.imageRes ?: R.drawable.mock_product_image,
        category = this?.category ?: "Бейдж",
        price = this?.price ?: "1 234 ₽",
        article = this?.article ?: "12345678",
        title = this?.name ?: "Название товара",
        rating = "4,5",
        ordersCount = "8 236",
        stockCount = "947",
        description = "Блузка-боди скимс без рукавов от Moremio – это идеальное сочетание стиля и комфорта, которое подчеркнет вашу женственность и красоту. Соблазнительный вырез на груди и обтягивающий крой создают представительскую и элегантную модель, идеально подходящую как для вечерних летних выходов, так и для повседневной носки. Мягкая вискоза в составе ткани обеспечит комфорт в течение всего дня, а эластан придаст блузке-боди идеальную посадку по фигуре. Модель skims с запахом и стрингами добавит пикантности вашему образу, а складки на талии – изысканности. Благодаря эластичному материалу блузка-боди прекрасно подойдет для высоких девушек и женщин с пышными формами. Это универсальная модель, которая легко трансформируется из офисного варианта в вечерний наряд, просто добавив стильные аксессуары. Это та самая летняя блузка-боди из pinterest (пинтерест)"
    )
}
