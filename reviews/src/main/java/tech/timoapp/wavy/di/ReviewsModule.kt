package tech.timoapp.wavy.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import tech.timoapp.wavy.data.local.AppDatabase
import tech.timoapp.wavy.data.repository.BalanceRepository
import tech.timoapp.wavy.data.repository.BalanceRepositoryImpl
import tech.timoapp.wavy.viewmodel.ReviewsViewModel

val reviewsModule = module {
    single { AppDatabase.getDatabase(androidContext()) }
    single { get<AppDatabase>().balanceDao() }
    single<BalanceRepository> { BalanceRepositoryImpl(get(), get()) }
    viewModel { ReviewsViewModel(get(), get()) }
}
