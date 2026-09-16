package tech.timoapp.wavy.data.repository

import tech.timoapp.wavy.data.local.dao.BalanceDao
import tech.timoapp.wavy.data.local.entity.toBalanceModel
import tech.timoapp.wavy.data.local.entity.toEntity
import tech.timoapp.wavy.model.toBalanceModel
import tech.timoapp.wavy.network.ApiService

private const val STALE_TIME = 5 * 36 * 1000

class BalanceRespository(private val apiService: ApiService, private val balanceDao: BalanceDao) {
    suspend fun getBalance(forceRefresh: Boolean = false): BalanceResult {
        try {
            val response = apiService.getBalanceInfo()
            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                balanceDao.insertBalance(dto.toEntity())
                return BalanceResult(dto.toBalanceModel(), isFromCache = false, isStale = false)
            }
        } catch (e: Exception) {
        }

        val cached = balanceDao.getBalance()
        if (cached != null) {
            val isStale = (System.currentTimeMillis() - cached.updatedAt) > STALE_TIME
            return BalanceResult(cached.toBalanceModel(), isFromCache = true, isStale = isStale)
        }

        throw IllegalStateException("Не удалось загрузить данные")
    }
}
