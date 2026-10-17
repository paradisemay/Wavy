package tech.timoapp.wavy.domain.usecase

import tech.timoapp.wavy.data.repository.ProductRepository
import tech.timoapp.wavy.domain.model.Product
import tech.timoapp.wavy.domain.model.toProduct

class AddProductUseCase(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(): Result<Product> {
        return productRepository.addProduct().map { dto ->
            dto.toProduct()
        }
    }
}
