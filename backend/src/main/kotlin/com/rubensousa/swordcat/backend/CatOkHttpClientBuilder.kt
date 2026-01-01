package com.rubensousa.swordcat.backend

import com.rubensousa.swordcat.backend.internal.AuthenticationInterceptor
import okhttp3.OkHttpClient

class CatOkHttpClientBuilder(
    private val apiKey: String,
) {

    fun build(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthenticationInterceptor(apiKey))
            .build()
    }

}
