package tech.timoapp.wavy.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import tech.timoapp.wavy.viewmodel.ReviewsViewModel

val reviewsModule = module {
    viewModel { ReviewsViewModel(get(), get()) }
}