package com.rubensousa.swordcat.backend

import com.google.common.truth.Truth.assertThat
import com.rubensousa.swordcat.domain.CatRequest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import mockwebserver3.MockResponse
import mockwebserver3.MockWebServer
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CatHttpSourceTest {

    @get:Rule
    private val mockWebServer = MockWebServer()

    private val backendModule = BackendModule()
    private val retrofit = backendModule.provideRetrofit(backendModule.provideOkHttpClient())
    private val httpSource = CatHttpSource(
        service = backendModule.provideCatService(retrofit),
        dispatcher = UnconfinedTestDispatcher()
    )
    private val defaultResponse = "cat_breed_response.json"
    private val defaultRequest = CatRequest(limit = 1, offset = 0)


    @Test
    fun `cats are loaded from remote source`() = runTest {
        // given
        enqueueResponse(defaultResponse)

        // when
        val output = httpSource.loadCats(defaultRequest).getOrThrow()

        // then
        val firstCat = output.first()
        assertThat(firstCat.id).isEqualTo("abys")
        assertThat(firstCat.breedName).isEqualTo("Abyssinian")
        assertThat(firstCat.origin).isEqualTo("Egypt")
        assertThat(firstCat.temperament).isEqualTo("Active, Energetic, Independent, Intelligent, Gentle")
        assertThat(firstCat.lifespan).isEqualTo(14..15)
        assertThat(firstCat.imageId).isEqualTo("0XYvRd7oD")
    }

    private fun enqueueResponse(filePath: String) {
        val localJson = readJson(filePath)
        val response = MockResponse.Builder()
            .code(200)
            .body(localJson)
            .build()
        mockWebServer.enqueue(response)
    }

    private fun readJson(path: String): String {
        return this::class.java.classLoader!!.getResourceAsStream(path)
            .bufferedReader()
            .use { reader ->
                reader.readText()
            }
    }

}