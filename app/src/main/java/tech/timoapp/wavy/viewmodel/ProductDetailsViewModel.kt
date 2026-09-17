package tech.timoapp.wavy.viewmodel

import androidx.lifecycle.ViewModel
import tech.timoapp.wavy.network.ApiService

class ProductDetailsViewModel(
    val apiService: ApiService
) : ViewModel() {
    suspend fun getProductDetails(id: String) = apiService.getProductDetails(id)
}
