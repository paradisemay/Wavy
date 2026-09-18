package tech.timoapp.wavy.viewmodel

import app.cash.turbine.test
import java.io.IOException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import tech.timoapp.wavy.FakeProductRepository
import tech.timoapp.wavy.MainDispatcherRule
import tech.timoapp.wavy.domain.usecase.AddProductUseCase
import tech.timoapp.wavy.domain.usecase.DeleteProductUseCase
import tech.timoapp.wavy.domain.usecase.GetProductsPagedUseCase
import tech.timoapp.wavy.model.ProductListUiEvent
import tech.timoapp.wavy.products.R
import tech.timoapp.wavy.productDto

@OptIn(ExperimentalCoroutinesApi::class)
class ProductListViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun addProduct_emitsAddedEventWhenRequestSucceeds() = runTest {
        val repository = FakeProductRepository().apply {
            addProductResult = Result.success(productDto())
        }
        val viewModel = createViewModel(repository)

        viewModel.eventFlow.test {
            viewModel.addProduct()

            assertEquals(ProductListUiEvent.ProductAdded, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun addProduct_emitsErrorEventWhenRequestFails() = runTest {
        val repository = FakeProductRepository().apply {
            addProductResult = Result.failure(IOException("offline"))
        }
        val viewModel = createViewModel(repository)

        viewModel.eventFlow.test {
            viewModel.addProduct()

            assertEquals(
                ProductListUiEvent.Error(message = "offline", messageRes = R.string.error_add_product),
                awaitItem()
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteProduct_emitsDeletedEventWhenRequestSucceeds() = runTest {
        val repository = FakeProductRepository()
        val viewModel = createViewModel(repository)

        viewModel.eventFlow.test {
            viewModel.deleteProduct("product-id")

            assertEquals(ProductListUiEvent.ProductDeleted, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun createViewModel(repository: FakeProductRepository) = ProductListViewModel(
        getProductsPagedUseCase = GetProductsPagedUseCase(repository),
        addProductUseCase = AddProductUseCase(repository),
        deleteProductUseCase = DeleteProductUseCase(repository)
    )
}
