package tech.timoapp.wavy.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.draw.clip
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
