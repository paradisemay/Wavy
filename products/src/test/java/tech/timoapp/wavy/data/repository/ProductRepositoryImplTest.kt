package tech.timoapp.wavy.data.repository

import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import retrofit2.Response
import tech.timoapp.wavy.network.ApiService
import tech.timoapp.wavy.network.dto.ProductsResponseDto
import tech.timoapp.wavy.productDto

class ProductRepositoryImplTest {
    private val apiService = mock(ApiService::class.java)
    private val repository = ProductRepositoryImpl(apiService)

    @Test
    fun getProducts_returnsProductsWhenRequestSucceeds() = runTest {
        val expected = listOf(productDto())
        `when`(apiService.getProducts()).thenReturn(
            Response.success(ProductsResponseDto(currentPage = 1, totalPages = 1, data = expected))
        )

        val result = repository.getProducts()

        assertTrue(result.isSuccess)
        assertEquals(expected, result.getOrThrow())
        verify(apiService).getProducts()
    }

    @Test
    fun getProducts_returnsFailureWhenNetworkRequestThrows() = runTest {
        val error = IllegalStateException("offline")
        `when`(apiService.getProducts()).thenThrow(error)

        val result = repository.getProducts()

        assertTrue(result.isFailure)
        assertEquals(error, result.exceptionOrNull())
    }

    @Test
    fun getProductDetails_returnsFailureWhenServerReturnsError() = runTest {
        `when`(apiService.getProductDetails("product-id")).thenReturn(
            Response.error(404, "not found".toResponseBody("text/plain".toMediaType()))
        )

        val result = repository.getProductDetails("product-id")

        assertTrue(result.isFailure)
        assertEquals(404, (result.exceptionOrNull() as tech.timoapp.wavy.network.ServerException).code)
    }

    @Test
    fun addProduct_returnsProductWhenRequestSucceeds() = runTest {
        val expected = productDto()
        `when`(apiService.generateProduct()).thenReturn(Response.success(expected))

        val result = repository.addProduct()

        assertEquals(expected, result.getOrThrow())
    }

    @Test
    fun deleteProduct_returnsNotRemovableFailureWhenServerRejectsDeletion() = runTest {
        `when`(apiService.deleteProduct("product-id")).thenReturn(
            Response.error(
                409,
                "product is not removable".toResponseBody("text/plain".toMediaType())
            )
        )

        val result = repository.deleteProduct("product-id")

        assertFalse(result.isSuccess)
        assertTrue(result.exceptionOrNull() is tech.timoapp.wavy.domain.model.ProductNotRemovableException)
    }
}
