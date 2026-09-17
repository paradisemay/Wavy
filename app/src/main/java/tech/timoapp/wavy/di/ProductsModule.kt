package tech.timoapp.wavy.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import tech.timoapp.wavy.data.repository.ProductRepository
import tech.timoapp.wavy.data.repository.ProductRepositoryImpl
import tech.timoapp.wavy.domain.usecase.AddProductUseCase
import tech.timoapp.wavy.domain.usecase.DeleteProductUseCase
import tech.timoapp.wavy.domain.usecase.GetProductDetailsUseCase
import tech.timoapp.wavy.domain.usecase.GetProductsUseCase
import tech.timoapp.wavy.viewmodel.ProductDetailsViewModel
import tech.timoapp.wavy.viewmodel.ProductListViewModel

val productsModule = module {
    single<ProductRepository> { ProductRepositoryImpl(get()) }
    factory { GetProductsUseCase(get()) }
    factory { GetProductDetailsUseCase(get()) }
    factory { AddProductUseCase(get()) }
    factory { DeleteProductUseCase(get()) }
    viewModel { ProductListViewModel(get(), get(), get()) }
    viewModel { ProductDetailsViewModel(get()) }
}