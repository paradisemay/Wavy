package tech.timoapp.wavy.di

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tech.timoapp.wavy.core.BuildConfig
import tech.timoapp.wavy.network.ApiService

val coreNetworkModule = module {
    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single {
        OkHttpClient.Builder().addInterceptor(get<HttpLoggingInterceptor>())
            .addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder().header("Authorization", BuildConfig.AUTH_TOKEN)
                    .header("Content-Type", "application/json")
                    .method(original.method, original.body).build()
                chain.proceed(request)
            }.build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<ApiService> {
        get<Retrofit>().create(ApiService::class.java)
    }
}

val networkModule = coreNetworkModule
