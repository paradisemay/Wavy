package tech.timoapp.wavy.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import tech.timoapp.wavy.model.BalanceModel
import tech.timoapp.wavy.model.toBalanceModel
import tech.timoapp.wavy.network.dto.BalanceInfoDto
import tech.timoapp.wavy.network.dto.SalesChartDto

@Entity(tableName = "balance")
data class BalanceEntity(
    @PrimaryKey val id: Int = 0,
    val balance: Double,
    val sales: Double,
    val income: Int,
    val shopRating: Double,
    val totalSalesCount: Int,
    val totalRefundsCount: Int,
    val totalRefundsPercent: Double,
    val monthlyRatingGrow: Double,
    val monthlySalesGrow: Int,
    val salesChartJson: String,
    val updatedAt: Long = System.currentTimeMillis()
)

fun BalanceInfoDto.toEntity(updatedAt: Long = System.currentTimeMillis()): BalanceEntity {
    return BalanceEntity(
        id = 0,
        balance = balance,
        sales = sales,
        income = income,
        shopRating = shopRating,
        totalSalesCount = totalSalesCount,
        totalRefundsCount = totalRefundsCount,
        totalRefundsPercent = totalRefundsPercent,
        monthlyRatingGrow = monthlyRatingGrow,
        monthlySalesGrow = monthlySalesGrow,
        salesChartJson = Gson().toJson(salesChart),
        updatedAt = updatedAt
    )
}

fun BalanceEntity.toBalanceModel(): BalanceModel {
    val chartDto = try {
        Gson().fromJson(salesChartJson, SalesChartDto::class.java)
    } catch (e: Exception) {
        null
    } ?: SalesChartDto(averageSales = 0.0, data = emptyList())

    val dto = BalanceInfoDto(
        shopId = "",
        balance = balance,
        sales = sales,
        income = income,
        shopRating = shopRating,
        totalSalesCount = totalSalesCount,
        totalRefundsCount = totalRefundsCount,
        totalRefundsPercent = totalRefundsPercent,
        monthlyRatingGrow = monthlyRatingGrow,
        monthlySalesGrow = monthlySalesGrow,
        salesChart = chartDto
    )
    return dto.toBalanceModel()
}
