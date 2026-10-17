package tech.timoapp.wavy.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun Modifier.shimmerEffect(
    shimmerColors: List<Color> = listOf(
        Color(0xFFE6E0E9),
        Color(0xFFF5F0F7),
        Color(0xFFE6E0E9)
    )
): Modifier {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_anim"
    )

    return this.background(
        brush = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(x = translateAnim - 500f, y = translateAnim - 500f),
            end = Offset(x = translateAnim, y = translateAnim)
        )
    )
}

@Composable
fun Modifier.purpleShimmerEffect(): Modifier = shimmerEffect(
    shimmerColors = listOf(
        Color(0xFFEADDFF),
        Color(0xFFF6EEFF),
        Color(0xFFEADDFF)
    )
)

@Composable
fun Modifier.softPurpleShimmerEffect(): Modifier = shimmerEffect(
    shimmerColors = listOf(
        Color(0xFFF3EDF7),
        Color(0xFFFCF9FC),
        Color(0xFFF3EDF7)
    )
)
