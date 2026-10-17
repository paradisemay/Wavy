package tech.timoapp.wavy.data.repository

import tech.timoapp.wavy.model.BalanceModel

data class BalanceResult (
    val balance: BalanceModel,
    val isFromCache: Boolean,
    val isStale: Boolean = false
)