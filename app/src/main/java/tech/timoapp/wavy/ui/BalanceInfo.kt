package tech.timoapp.wavy.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.timoapp.wavy.R
import tech.timoapp.wavy.model.BalanceModel
import tech.timoapp.wavy.model.SalesBarModel

@Composable
fun MetricItem(value: String, label: String, badge: String? = null, badgeColor: Color? = null) {
    val badgeColor = badgeColor ?: Color.Unspecified
    Column(
        modifier = Modifier
    ) {
        MetricTitleText(value)
        MetricMainText(label)
        if (badge != null) {
            MetricBadgeText(badge, badgeColor)
        }
    }
}

@Composable
fun BalanceCard(balance: BalanceModel, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = colorResource(R.color.light_purple), shape = RoundedCornerShape(8.dp)
            )
            .padding(
                start = 8.dp, top = 8.dp, end = 8.dp, bottom = 16.dp
            )
    ) {
        MainStatisticText("Баланс")
        HugeText(balance.balance)
        Spacer(modifier = Modifier.size(size = 16.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.Start),
            modifier = Modifier.fillMaxWidth()
        ) {
            MetricItem(
                balance.shopRating,
                "рейтинг",
                balance.ratingGrow,
                if (balance.isRatingGrowPositive) colorResource(R.color.green_text_color)
                else colorResource(R.color.red_text_color)
            )

            MetricItem(
                balance.totalSalesCount,
                "продано",
                balance.salesGrow,
                if (balance.isSalesGrowPositive) colorResource(R.color.green_text_color)
                else colorResource(R.color.red_text_color)
            )

            MetricItem(
                balance.totalRefundsCount, "возврата", balance.totalRefundsPercent
            )

            MetricItem(
                balance.income, "выручка"
            )
        }
    }
}

val mockSalesBars = listOf(
    SalesBarModel(monthName = "Июнь", amountFormatted = "30 511 ₽", fraction = 0.55f),
    SalesBarModel(monthName = "Июль", amountFormatted = "22 281 ₽", fraction = 0.40f),
    SalesBarModel(monthName = "Август", amountFormatted = "55 210 ₽", fraction = 1.0f),
    SalesBarModel(
        monthName = "Сентябрь", amountFormatted = "55 210 ₽", fraction = 1.0f, growthBadge = "+9,8%"
    )
)

val balanceModel = BalanceModel(
    balance = "574 229,54 ₽",
    sales = "95 445 ₽",
    income = "85 001 ₽",
    shopRating = "4,87",
    ratingGrow = "↗ 0,04",
    isRatingGrowPositive = true,
    totalSalesCount = "24 236",
    salesGrow = "+ 447",
    isSalesGrowPositive = true,
    totalRefundsCount = "442",
    totalRefundsPercent = "12,5%",
    chartBars = mockSalesBars
)

@Preview(showBackground = true)
@Composable
fun MetricItemPreview() {
    MetricItem(
        value = "4,87",
        label = "рейтинг",
        badge = "↗ 0,04",
        badgeColor = colorResource(R.color.green_text_color)
    )
}

@Preview(showBackground = true)
@Composable
fun BalanceCardPreview() {
    BalanceCard(
        balance = balanceModel, modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun ChartItem(bar: SalesBarModel, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(bar.fraction.dp * 49)
                .background(colorResource(R.color.dark_purple))
        ) {
            if (bar.growthBadge != null) {
                ChartText(
                    bar.growthBadge,
                    color = colorResource(R.color.white),
                    modifier = Modifier.padding(top = 2.dp, start = 2.dp)
                )
            }
        }
        Spacer(modifier = Modifier.size(2.dp))
        ChartText(bar.monthName)
        ChartText(bar.amountFormatted)
    }
}

@Preview(showBackground = true)
@Composable
fun ChartItemPreview() {
    ChartItem(
        bar = SalesBarModel(
            monthName = "Сентябрь",
            amountFormatted = "55 210 ₽",
            fraction = 1.0f,
            growthBadge = "+9,8%"
        ), modifier = Modifier
            .padding(16.dp)
            .size(width = 80.dp, height = 80.dp)
    )
}

@Composable
fun SalesChartCard(balance: BalanceModel, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = colorResource(R.color.light_purple), shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
    ) {
        MainStatisticText("Продажи")
        MainBoldStatisticText(balance.sales)
        Spacer(modifier = Modifier.size(4.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
        ) {
            balance.chartBars.forEach { bar ->
                ChartItem(
                    bar = bar, modifier = Modifier.weight(1f).align(Alignment.Bottom)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SalesChartCardPreview() {
    SalesChartCard(
        balance = balanceModel, modifier = Modifier.padding(16.dp)
    )
}