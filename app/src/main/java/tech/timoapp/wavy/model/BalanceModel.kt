package tech.timoapp.wavy.model

import tech.timoapp.wavy.network.dto.BalanceInfoDto
import tech.timoapp.wavy.network.dto.SalesPointDto
import tech.timoapp.wavy.util.formatPrice
import tech.timoapp.wavy.util.formatRating
import java.util.Locale
import kotlin.math.roundToInt

data class BalanceModel(
    val balance: String,
    val sales: String,
    val income: String,
    val shopRating: String,
    val ratingGrow: String,
    val isRatingGrowPositive: Boolean,
    val totalSalesCount: String,
    val salesGrow: String,
    val isSalesGrowPositive: Boolean,
    val totalRefundsCount: String,
    val totalRefundsPercent: String,
    val chartBars: List<SalesBarModel>
)

data class SalesBarModel(
    val monthName: String,
    val amountFormatted: String,
    val fraction: Float,
    val growthBadge: String? = null
)

fun BalanceInfoDto.toBalanceModel(): BalanceModel {
    val maxAmount = salesChart.data.maxOfOrNull { it.amount }?.takeIf { it > 0.0 } ?: 1.0

    val bars = salesChart.data.mapIndexed { index, point ->
        val month = parseMonthName(point.period)
        val amount = point.amount.formatPrice()
        val fraction = (point.amount / maxAmount).toFloat().coerceIn(0.1f, 1.0f)
        val isLast = index == salesChart.data.lastIndex
        val badge = if (isLast && monthlySalesGrow != 0) {
            val prefix = if (monthlySalesGrow > 0) "+" else ""
            val prevAmount = salesChart.data.getOrNull(index - 1)?.amount ?: point.amount
            if (prevAmount > 0) {
                val percent = ((point.amount - prevAmount) / prevAmount) * 100
                String.format(Locale.US, "%s%.1f%%", prefix, percent).replace('.', ',')
            } else null
        } else null

        SalesBarModel(
            monthName = month,
            amountFormatted = amount,
            fraction = fraction,
            growthBadge = badge
        )
    }

    return BalanceModel(
        balance = formatBalance(this.balance),
        sales = this.sales.formatPrice(),
        income = this.income.toDouble().formatPrice(),
        shopRating = String.format(Locale.US, "%.2f", this.shopRating).replace('.', ','),
        ratingGrow = formatGrow(this.monthlyRatingGrow, isPercent = false, arrow = true),
        isRatingGrowPositive = this.monthlyRatingGrow >= 0,
        totalSalesCount = formatCount(this.totalSalesCount),
        salesGrow = formatGrow(this.monthlySalesGrow.toDouble(), isPercent = false, arrow = false),
        isSalesGrowPositive = this.monthlySalesGrow >= 0,
        totalRefundsCount = formatCount(this.totalRefundsCount),
        totalRefundsPercent = String.format(Locale.US, "%.1f%%", this.totalRefundsPercent).replace('.', ','),
        chartBars = bars
    )
}

private fun formatBalance(balance: Double): String {
    val intPart = balance.toLong()
    val fracPart = ((balance - intPart) * 100).roundToInt().coerceIn(0, 99)
    val formattedInt = String.format(Locale.US, "%,d", intPart).replace(',', ' ')
    return String.format(Locale.US, "%s,%02d ₽", formattedInt, fracPart)
}

private fun formatCount(count: Int): String {
    return String.format(Locale.US, "%,d", count).replace(',', ' ')
}

private fun formatGrow(value: Double, isPercent: Boolean, arrow: Boolean): String {
    val sign = if (value >= 0) if (arrow) "↗ " else "+ " else if (arrow) "↘ " else "- "
    val absValue = kotlin.math.abs(value)
    val formatted = if (isPercent || absValue % 1.0 != 0.0) {
        String.format(Locale.US, "%.2f", absValue).replace('.', ',')
    } else {
        absValue.toInt().toString()
    }
    return "$sign$formatted"
}

private fun parseMonthName(period: String): String {
    val monthMap = mapOf(
        "01" to "Январь", "02" to "Февраль", "03" to "Март",
        "04" to "Апрель", "05" to "Май", "06" to "Июнь",
        "07" to "Июль", "08" to "Август", "09" to "Сентябрь",
        "10" to "Октябрь", "11" to "Ноябрь", "12" to "Декабрь"
    )
    val parts = period.split("-")
    if (parts.size >= 2) {
        val monthCode = parts[1]
        return monthMap[monthCode] ?: "Месяц"
    }
    return "Месяц"
}
