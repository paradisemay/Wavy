package tech.timoapp.wavy.domain.usecase

import tech.timoapp.wavy.data.repository.ProductRepository

class DeleteProductUseCase(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(id: String): Result<Unit> {
        return productRepository.deleteProduct(id)
    }
}
