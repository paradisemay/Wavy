package tech.timoapp.wavy.network

object ApiClient {
    val apiService: ApiService
        get() = RetrofitClient.apiService
}
