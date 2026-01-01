package com.rubensousa.swordcat.backend.internal

import okhttp3.Interceptor
import okhttp3.Response

internal class AuthenticationInterceptor(
    private val apiKey: String,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        requestBuilder.header("x-api-key", apiKey)
        return chain.proceed(requestBuilder.build())
    }
}