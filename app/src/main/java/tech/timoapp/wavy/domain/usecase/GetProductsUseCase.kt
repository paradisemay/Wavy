package tech.timoapp.wavy.domain.usecase

import tech.timoapp.wavy.data.repository.ProductRepository
import tech.timoapp.wavy.domain.model.Product
import tech.timoapp.wavy.domain.model.toProduct

class GetProductsUseCase(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(): Result<List<Product>> {
        return productRepository.getProducts().map { dtoList ->
            dtoList.map { it.toProduct() }
        }
    }
}
