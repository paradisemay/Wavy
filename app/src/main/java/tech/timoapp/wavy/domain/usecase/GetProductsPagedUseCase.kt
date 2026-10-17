package tech.timoapp.wavy.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import tech.timoapp.wavy.data.repository.ProductRepository
import tech.timoapp.wavy.domain.model.Product

class GetProductsPagedUseCase(
    private val repository: ProductRepository
) {
    operator fun invoke(): Flow<PagingData<Product>> {
        return repository.getProductsPaged()
    }
}
