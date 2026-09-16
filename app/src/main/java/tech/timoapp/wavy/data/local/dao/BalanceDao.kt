package tech.timoapp.wavy.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tech.timoapp.wavy.data.local.entity.BalanceEntity

@Dao
interface BalanceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBalance(balance: BalanceEntity)

    @Query("SELECT * FROM balance WHERE id = 0 LIMIT 1")
    suspend fun getBalance(): BalanceEntity?

    @Query("DELETE FROM balance")
    suspend fun clearBalance(): Int
}
