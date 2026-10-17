package tech.timoapp.wavy.domain.usecase

import java.io.IOException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import tech.timoapp.wavy.FakeProductRepository
import tech.timoapp.wavy.productDetailsDto
import tech.timoapp.wavy.productDto

class ProductUseCasesTest {
    @Test
    fun getProducts_mapsDtosToDomainModels() = runTest {
        val repository = FakeProductRepository().apply {
            productsResult = Result.success(listOf(productDto(price = 1_250.0)))
        }

        val result = GetProductsUseCase(repository)()

        assertEquals("1 250 ₽", result.getOrThrow().single().price)
        assertEquals("https://image.example.com/product.jpg", result.getOrThrow().single().imageUrl)
    }

    @Test
    fun getProductDetails_preservesRepositoryFailure() = runTest {
        val error = IOException("offline")
        val repository = FakeProductRepository().apply {
            productDetailsResult = Result.failure(error)
        }

        val result = GetProductDetailsUseCase(repository)("product-id")

        assertTrue(result.isFailure)
        assertEquals(error, result.exceptionOrNull())
    }

    @Test
    fun addProduct_mapsCreatedProduct() = runTest {
        val repository = FakeProductRepository().apply {
            addProductResult = Result.success(productDto())
        }

        val result = AddProductUseCase(repository)()

        assertEquals("product-id", result.getOrThrow().id)
    }

    @Test
    fun deleteProduct_forwardsProductIdAndSuccess() = runTest {
        val repository = FakeProductRepository()

        val result = DeleteProductUseCase(repository)("product-id")

        assertTrue(result.isSuccess)
    }
}
