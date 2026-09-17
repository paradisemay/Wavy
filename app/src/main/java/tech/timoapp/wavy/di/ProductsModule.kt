package tech.timoapp.wavy.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import tech.timoapp.wavy.data.repository.ProductRepository
import tech.timoapp.wavy.data.repository.ProductRepositoryImpl
import tech.timoapp.wavy.viewmodel.ProductDetailsViewModel
import tech.timoapp.wavy.viewmodel.ProductListViewModel

val productsModule = module {
    single<ProductRepository> { ProductRepositoryImpl(get()) }
    viewModel { ProductListViewModel(get()) }
    viewModel { ProductDetailsViewModel(get()) }
}