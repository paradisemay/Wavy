package tech.timoapp.wavy.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.timoapp.wavy.R

@Composable
fun FactorIdBlock(largeText: String, smallText: String, iconRes: Int) {
    Column {
        Row {
            LargeStatisticText(
                text = largeText, modifier = Modifier.padding(end = 4.dp)
            )
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(26.21.dp)
            )
        }
        MainStatisticText(smallText)
    }
}

@Composable
fun RoundedImage(imageRes: Int) {
    Image(
        painter = painterResource(imageRes),
        contentDescription = null,
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
    )
}

@Composable
fun Badge(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(
        modifier = modifier
            .background(
                color = colorResource(R.color.white), shape = RoundedCornerShape(48.dp)
            )
            .padding(
                horizontal = 9.dp, vertical = 2.dp
            )
    ) {
        content()
    }
}


@Preview(showBackground = true)
@Composable
fun RoundedImagePreview() {
    RoundedImage(
        imageRes = R.drawable.mock_product_image
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

@Composable
fun DescriptionContent(
    modifier: Modifier = Modifier,
    content: @Composable (isExpanded: Boolean) -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }

    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "chevronRotation"
    )

    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded }
            .animateContentSize()
    ) {
        content(isExpanded)

        Icon(
            painter = painterResource(id = R.drawable.ic_chevron_down),
            contentDescription = null,
            modifier = Modifier
                .background(colorResource(R.color.background_color))
                .size(26.21.dp)
                .rotate(rotationAngle)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun DescriptionContentPreview() {
    DescriptionContent {isExpanded ->
        MainText(
            text = "Блузка-боди скимс без рукавов от Moremio – это идеальное сочетание стиля и комфорта, которое подчеркнет вашу женственность и красоту. Соблазнительный вырез на груди и обтягивающий крой создают сексуальный и элегантный образ, идеально подходящий как для вечерних летних выходов, так и для повседневной носки. Мягкая вискоза в составе ткани обеспечит комфорт в течение всего дня, а эластан придаст блузке-боди идеальную посадку по фигуре. Модель skims с запахом и стрингами добавит пикантности вашему образу, а складки на талии – изысканности. Благодаря эластичному материалу блузка-боди прекрасно подойдет для высоких девушек и женщин с пышными формами. Это универсальная модель, которая легко трансформируется из офисного варианта в вечерний наряд, просто добавив стильные аксессуары. Это та самая летняя блузка-боди из pinterest (пинтерест)",
            maxLines = if (isExpanded) Int.MAX_VALUE else 7
        )
    }
}
