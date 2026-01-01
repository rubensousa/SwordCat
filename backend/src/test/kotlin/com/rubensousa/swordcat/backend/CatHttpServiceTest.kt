package com.rubensousa.swordcat.backend

import com.google.common.truth.Truth.assertThat
import com.rubensousa.swordcat.domain.CatRequest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import mockwebserver3.MockWebServer
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CatHttpServiceTest {

    private val mockWebServer = MockWebServer()
    private val okHttpClient = CatOkHttpClientBuilder("test").build()
    private val retrofit = CatRetrofitBuilder(okHttpClient).build()
    private val service = CatHttpService(
        retrofit = retrofit,
        dispatcher = UnconfinedTestDispatcher()
    )
    private val defaultResponse = "cat_breed_response.json"
    private val defaultRequest = CatRequest(limit = 1, offset = 0)


    @Test
    fun `cats are loaded from remote source`() = runTest {
        // given
        mockWebServer.enqueueResponse(defaultResponse)

        // when
        val output = service.loadCats(defaultRequest).getOrThrow()

        // then
        val firstCat = output.first()
        assertThat(firstCat.id).isEqualTo("abys")
        assertThat(firstCat.breedName).isEqualTo("Abyssinian")
        assertThat(firstCat.origin).isEqualTo("Egypt")
        assertThat(firstCat.temperament)
            .isEqualTo("Active, Energetic, Independent, Intelligent, Gentle")
        assertThat(firstCat.lifespan).isEqualTo(14..15)
        assertThat(firstCat.imageId).isEqualTo("0XYvRd7oD")
    }

}
