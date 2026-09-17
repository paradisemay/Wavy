package tech.timoapp.wavy.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import tech.timoapp.wavy.core.R

@Composable
fun BaseText(
    text: String,
    color: Color,
    fontSize: TextUnit,
    fontWeight: FontWeight = FontWeight(500),
    maxLines: Int? = 1,
    modifier: Modifier? = null
) {
    Text(
        text = text,
        fontWeight = fontWeight,
        fontSize = fontSize,
        color = color,
        maxLines = maxLines ?: Int.MAX_VALUE,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier ?: Modifier
    )
}

@Composable
fun MetricTitleText(text: String, modifier: Modifier? = null, maxLines: Int? = 1) {
    BaseText(
        text,
        colorResource(R.color.main_text_color),
        16.sp,
        fontWeight = FontWeight(600),
        maxLines = maxLines,
        modifier = modifier
    )
}

@Composable
fun MetricMainText(text: String, modifier: Modifier? = null, maxLines: Int? = 1) {
    BaseText(
        text,
        colorResource(R.color.main_text_color),
        12.sp,
        maxLines = maxLines,
        modifier = modifier
    )
}

@Composable
fun MetricBadgeText(
    text: String,
    color: Color = colorResource(R.color.main_text_color),
    modifier: Modifier? = null,
    maxLines: Int? = 1
) {
    BaseText(
        text,
        color = color,
        fontSize = 14.sp,
        fontWeight = FontWeight(400),
        maxLines = maxLines,
        modifier = modifier
    )
}

@Composable
fun ReviewStarsText(
    text: String,
    color: Color = colorResource(R.color.main_text_color),
    modifier: Modifier? = null,
    maxLines: Int? = 1
) {
    BaseText(
        text,
        color = color,
        fontSize = 14.sp,
        maxLines = maxLines,
        modifier = modifier
    )
}

@Composable
fun HugeText(
    text: String,
    color: Color = colorResource(R.color.main_text_color),
    modifier: Modifier? = null,
    maxLines: Int? = 1
) {
    BaseText(
        text,
        color = color,
        fontSize = 45.sp,
        maxLines = maxLines,
        modifier = modifier
    )
}

@Composable
fun ChartText(
    text: String,
    color: Color = colorResource(R.color.main_text_color),
    modifier: Modifier? = null,
    maxLines: Int? = 1
) {
    BaseText(
        text,
        color = color,
        fontSize = 11.sp,
        maxLines = maxLines,
        modifier = modifier
    )
}

@Composable
fun TitleText(text: String, modifier: Modifier? = null, maxLines: Int? = 1) {
    BaseText(
        text,
        colorResource(R.color.main_text_color),
        24.sp,
        maxLines = maxLines,
        modifier = modifier
    )
}

@Composable
fun LargeText(text: String, modifier: Modifier? = null, maxLines: Int? = 2) {
    BaseText(
        text,
        colorResource(R.color.main_text_color),
        22.sp,
        maxLines = maxLines,
        modifier = modifier
    )
}

@Composable
fun MainText(text: String, modifier: Modifier? = null, maxLines: Int? = null) {
    BaseText(
        text,
        colorResource(R.color.main_text_color),
        16.sp,
        FontWeight(400),
        maxLines = maxLines,
        modifier = modifier
    )
}

@Composable
fun MainStatisticText(text: String, modifier: Modifier? = null, maxLines: Int? = 1) {
    BaseText(
        text,
        colorResource(R.color.main_text_color),
        16.sp,
        maxLines = maxLines,
        modifier = modifier
    )
}

@Composable
fun MainBoldStatisticText(text: String, modifier: Modifier? = null, maxLines: Int? = 1) {
    BaseText(
        text,
        colorResource(R.color.main_text_color),
        16.sp,
        fontWeight = FontWeight(600),
        maxLines = maxLines,
        modifier = modifier
    )
}

@Composable
fun LargeStatisticText(text: String, modifier: Modifier? = null, maxLines: Int? = 1) {
    BaseText(
        text,
        colorResource(R.color.main_text_color),
        26.sp,
        maxLines = maxLines,
        modifier = modifier
    )
}

@Composable
fun TitleSubText(text: String, modifier: Modifier? = null, maxLines: Int? = 1) {
    BaseText(
        text,
        colorResource(R.color.sub_text_color),
        24.sp,
        maxLines = maxLines,
        modifier = modifier
    )
}
