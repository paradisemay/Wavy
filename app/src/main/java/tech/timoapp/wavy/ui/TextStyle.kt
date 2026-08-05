package tech.timoapp.wavy.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import tech.timoapp.wavy.R

@Composable
fun BaseText(
    text: String,
    color: Color,
    fontSize: TextUnit,
    fontWeight: FontWeight = FontWeight(500),
    maxLines: Int? = 1
) {
    Text(
        text = text,
        fontWeight = fontWeight,
        fontSize = fontSize,
        color = color,
        maxLines = maxLines ?: Int.MAX_VALUE,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
fun TitleText(text: String) {
    BaseText(text, colorResource(R.color.main_text_color), 24.sp)
}

@Composable
fun LargeText(text: String) {
    BaseText(text, colorResource(R.color.main_text_color), 22.sp, maxLines = 2)
}

@Composable
fun MainText(text: String) {
    BaseText(text, colorResource(R.color.main_text_color), 16.sp, FontWeight(400), maxLines = null)
}

@Composable
fun TitleSubText(text: String) {
    BaseText(text, colorResource(R.color.sub_text_color), 24.sp)
}
