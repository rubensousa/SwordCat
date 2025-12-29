package com.rubensousa.swordcat.backend.internal

import okhttp3.Interceptor
import okhttp3.Response

private const val API_KEY = "live_hq1UFxUSjbMx7tokbsRAKbESVLmb1PYB9wOFZw4ZFAX1HLyvikmkT5n8fuqdLxRc"

internal class AuthenticationInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        requestBuilder.header("x-api-key", API_KEY)
        return chain.proceed(requestBuilder.build())
    }
}