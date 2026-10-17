package tech.timoapp.wavy.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import tech.timoapp.wavy.data.local.dao.BalanceDao
import tech.timoapp.wavy.data.local.entity.BalanceEntity
import tech.timoapp.wavy.model.BalanceModel

@Database(entities = [BalanceEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun balanceDao(): BalanceDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "wavy_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}