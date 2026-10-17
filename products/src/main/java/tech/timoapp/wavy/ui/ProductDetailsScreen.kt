package tech.timoapp.wavy.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.androidx.compose.koinViewModel
import tech.timoapp.wavy.model.ProductDetailsUiState
import tech.timoapp.wavy.viewmodel.ProductDetailsViewModel
import tech.timoapp.wavy.products.R
import tech.timoapp.wavy.model.ProductDetailsState

@Composable
fun ProductDetailsRoute(
    productId: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProductDetailsViewModel = koinViewModel()
) {
    LaunchedEffect(productId) {
        viewModel.loadProduct(productId)
    }

    val state by viewModel.uiState.collectAsState()
    when (val currentState = state) {
        is ProductDetailsUiState.Success -> {
            DetailsPageScreen(
                product = currentState.product,
                onBackClick = onBackClick
            )
        }
        is ProductDetailsUiState.Error -> {
            ErrorStateScreen(
                onRetry = { viewModel.loadProduct(productId) },
                onBackClick = onBackClick
            )
        }
        is ProductDetailsUiState.Loading -> {
            DetailsPageSkeleton(
                onBackClick = onBackClick
            )
        }
    }
}

@Composable
fun TitleAndPriceContent(product: ProductDetailsState) {
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
fun StatisticContent(product: ProductDetailsState) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
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
fun ImageContent(product: ProductDetailsState) {
    Box(
        contentAlignment = Alignment.BottomStart,
    ) {
        RoundedImage(product.imageUrl)
        ContentBadge(product)
    }
}

@Composable
fun ContentBadge(product: ProductDetailsState) {
    Badge(
        modifier = Modifier.padding(10.dp)
    ) {
        MainText(product.category)
    }
}

@Composable
fun ProductDescription(product: ProductDetailsState) {
    DescriptionContent { isExpanded ->
        MainText(
            text = product.description,
            maxLines = if (isExpanded) Int.MAX_VALUE else DESCRIPTION_COLLAPSED_MAX_LINES
        )
    }
}

@Composable
fun DetailsPageScreen(
    product: ProductDetailsState, onBackClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background_color))
            .animateContentSize()
            .verticalScroll(scrollState)
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .padding(start = 4.dp, top = 8.dp, bottom = 8.dp)
                .size(48.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = null,
                modifier = Modifier.padding(16.dp)
            )
        }
        ImageContent(product)
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp)
        ) {
            TitleAndPriceContent(product)
            Spacer(modifier = Modifier.size(16.dp))
            StatisticContent(product)
            Spacer(modifier = Modifier.size(24.dp))
            ProductDescription(product)
        }
    }
}

@Composable
fun DetailsPageSkeleton(onBackClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background_color))
            .verticalScroll(rememberScrollState())
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .padding(start = 4.dp, top = 8.dp, bottom = 8.dp)
                .size(48.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = null,
                modifier = Modifier.padding(16.dp)
            )
        }

        Box(contentAlignment = Alignment.BottomStart) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shimmerEffect()
            )
            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .width(80.dp)
                    .height(24.dp)
                    .clip(RoundedCornerShape(48.dp))
                    .background(colorResource(R.color.white))
            )
        }

        Column(modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .width(130.dp)
                        .height(24.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .shimmerEffect()
                )
                Box(
                    modifier = Modifier
                        .width(70.dp)
                        .height(16.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .shimmerEffect()
                )
            }
            Spacer(modifier = Modifier.size(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(22.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.size(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(colorResource(R.color.light_purple))
            )
            Spacer(modifier = Modifier.size(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .shimmerEffect()
            )
        }
    }
}

val mockProduct = ProductDetailsState(
    imageUrl = "",
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

@Preview
@Composable
fun DetailsPageScreenPreview() {
    DetailsPageScreen(
        product = mockProduct, onBackClick = {})
}

@Preview
@Composable
fun DetailsPageSkeletonPreview() {
    DetailsPageSkeleton(onBackClick = {})
}