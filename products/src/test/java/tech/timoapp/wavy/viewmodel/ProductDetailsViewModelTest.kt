package tech.timoapp.wavy.viewmodel

import java.io.IOException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import tech.timoapp.wavy.FakeProductRepository
import tech.timoapp.wavy.MainDispatcherRule
import tech.timoapp.wavy.domain.usecase.GetProductDetailsUseCase
import tech.timoapp.wavy.model.ProductDetailsUiState
import tech.timoapp.wavy.productDetailsDto

@OptIn(ExperimentalCoroutinesApi::class)
class ProductDetailsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun loadProduct_setsSuccessStateWhenRequestSucceeds() = runTest {
        val repository = FakeProductRepository().apply {
            productDetailsResult = Result.success(productDetailsDto())
        }
        val viewModel = ProductDetailsViewModel(GetProductDetailsUseCase(repository))

        viewModel.loadProduct("product-id")

        val state = viewModel.uiState.value
        assertTrue(state is ProductDetailsUiState.Success)
        assertEquals("Товар", (state as ProductDetailsUiState.Success).product.title)
    }

    @Test
    fun loadProduct_setsErrorStateWhenRequestFails() = runTest {
        val repository = FakeProductRepository().apply {
            productDetailsResult = Result.failure(IOException("offline"))
        }
        val viewModel = ProductDetailsViewModel(GetProductDetailsUseCase(repository))

        viewModel.loadProduct("product-id")

        assertEquals(ProductDetailsUiState.Error("offline"), viewModel.uiState.value)
    }
}
