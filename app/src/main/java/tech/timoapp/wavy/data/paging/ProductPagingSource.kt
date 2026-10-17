package tech.timoapp.wavy.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import tech.timoapp.wavy.domain.model.ServerException
import tech.timoapp.wavy.network.ApiService
import tech.timoapp.wavy.network.dto.ProductDto

class ProductPagingSource(
    private val apiService: ApiService
) : PagingSource<Int, ProductDto>() {

    override fun getRefreshKey(state: PagingState<Int, ProductDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductDto> {
        val page = params.key ?: 1
        return try {
            val response = apiService.getProducts(page = page)
            val body = response.body()
            if (response.isSuccessful && body != null) {
                val prevKey = if (page > 1) page - 1 else null
                val nextKey = if (page < body.totalPages) page + 1 else null
                LoadResult.Page(
                    data = body.data,
                    prevKey = prevKey,
                    nextKey = nextKey
                )
            } else {
                LoadResult.Error(ServerException(response.code()))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
