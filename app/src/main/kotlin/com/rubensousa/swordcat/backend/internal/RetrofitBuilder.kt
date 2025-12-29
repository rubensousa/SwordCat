package com.rubensousa.swordcat.backend.internal

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

private const val API_BASE_URL = "https://api.thecatapi.com/v1/"

class RetrofitBuilder(
    private val okHttpClient: OkHttpClient
) {

    fun build(): Retrofit {
        val json = Json {
            explicitNulls = false
            ignoreUnknownKeys = true
        }
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(API_BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

}
