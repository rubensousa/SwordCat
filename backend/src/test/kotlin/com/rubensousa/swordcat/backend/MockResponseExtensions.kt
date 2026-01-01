package com.rubensousa.swordcat.backend

import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer

object MockResponseExtensions {

    fun readJson(path: String): String {
        return this::class.java.classLoader!!.getResourceAsStream(path)
            .bufferedReader()
            .use { reader ->
                reader.readText()
            }
    }
}

fun MockWebServer.enqueueResponse(filePath: String) {
    val localJson = MockResponseExtensions.readJson(filePath)
    val response = MockResponse.Builder()
        .code(200)
        .body(localJson)
        .build()
    enqueue(response)
}

