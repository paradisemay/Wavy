package tech.timoapp.wavy.network.dto

import com.google.gson.annotations.SerializedName

data class BalanceInfoDto(
    @SerializedName("shopId")
    val shopId: String,

    @SerializedName("balance")
    val balance: Double,

    @SerializedName("sales")
    val sales: Double,

    @SerializedName("income")
    val income: Int,

    @SerializedName("shopRating")
    val shopRating: Double,

    @SerializedName("totalSalesCount")
    val totalSalesCount: Int,

    @SerializedName("totalRefundsCount")
    val totalRefundsCount: Int,

    @SerializedName("totalRefundsPercent")
    val totalRefundsPercent: Double,

    @SerializedName("monthlyRatingGrow")
    val monthlyRatingGrow: Double,

    @SerializedName("monthlySalesGrow")
    val monthlySalesGrow: Int,

    @SerializedName("salesChart")
    val salesChart: SalesChartDto
)

data class SalesPointDto(
    @SerializedName("amount")
    val amount: Double,

    @SerializedName("period")
    val period: String
)

data class SalesChartDto(
    @SerializedName("averageSales")
    val averageSales: Double,

    @SerializedName("data")
    val data: List<SalesPointDto>,
)
