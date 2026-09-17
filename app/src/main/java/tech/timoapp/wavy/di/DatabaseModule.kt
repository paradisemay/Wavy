package tech.timoapp.wavy.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import tech.timoapp.wavy.data.local.AppDatabase
import tech.timoapp.wavy.data.repository.BalanceRepository

val databaseModule = module {
    single { AppDatabase.getDatabase(androidContext()) }
    single { get<AppDatabase>().balanceDao() }
    single { BalanceRepository(get(), get()) }
}