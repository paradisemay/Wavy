package tech.timoapp.wavy

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import tech.timoapp.wavy.data.repository.ProductRepository
import tech.timoapp.wavy.domain.model.Product
import tech.timoapp.wavy.network.dto.ProductDetailsDto
import tech.timoapp.wavy.network.dto.ProductDto

fun productDto(
    id: String = "product-id",
    price: Double = 1_250.0
) = ProductDto(
    id = id,
    name = "Товар",
    article = "article",
    category = "Категория",
    imageUrl = "image.example.com/product.jpg",
    isRemovable = true,
    oldPrice = null,
    price = price
)

fun productDetailsDto(id: String = "product-id") = ProductDetailsDto(
    id = id,
    name = "Товар",
    article = "article",
    category = "Категория",
    description = "Описание",
    imageUrl = "image.example.com/product.jpg",
    oldPrice = null,
    price = 1_250.0,
    rating = 4.5,
    stockCount = 12,
    ordersCount = 24
)

class FakeProductRepository : ProductRepository {
    var productsResult: Result<List<ProductDto>> = Result.success(emptyList())
    var productDetailsResult: Result<ProductDetailsDto> = Result.success(productDetailsDto())
    var addProductResult: Result<ProductDto> = Result.success(productDto())
    var deleteProductResult: Result<Unit> = Result.success(Unit)

    override suspend fun getProducts(): Result<List<ProductDto>> = productsResult

    override fun getProductsPaged(): Flow<PagingData<Product>> = flowOf(PagingData.empty())

    override suspend fun getProductDetails(id: String): Result<ProductDetailsDto> = productDetailsResult

    override suspend fun addProduct(): Result<ProductDto> = addProductResult

    override suspend fun deleteProduct(id: String): Result<Unit> = deleteProductResult
}
