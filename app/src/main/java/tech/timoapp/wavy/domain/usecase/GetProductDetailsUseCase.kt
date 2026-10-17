package tech.timoapp.wavy.domain.usecase

import tech.timoapp.wavy.data.repository.ProductRepository
import tech.timoapp.wavy.domain.model.ProductDetailsState
import tech.timoapp.wavy.domain.model.toDetailsModel

class GetProductDetailsUseCase(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(id: String): Result<ProductDetailsState> {
        return productRepository.getProductDetails(id).map { dto ->
            dto.toDetailsModel()
        }
    }
}
