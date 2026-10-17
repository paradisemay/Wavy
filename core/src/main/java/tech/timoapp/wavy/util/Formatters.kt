package tech.timoapp.wavy.util

import java.util.Locale
import kotlin.math.roundToInt

fun Double.formatPrice(): String {
    val rounded = this.roundToInt()
    return String.format(Locale.US, "%,d", rounded).replace(',', ' ') + " ₽"
}

fun Double.formatRating(): String {
    return String.format(Locale.US, "%.1f", this).replace(".", ",")
}
