package tech.timoapp.wavy.viewmodel

import androidx.lifecycle.ViewModel
import tech.timoapp.wavy.network.ApiService

class ProductListViewModel(
    val apiService: ApiService
) : ViewModel() {
    suspend fun getProducts() = apiService.getProducts()
}
