package com.rubensousa.swordcat.backend

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import mockwebserver3.MockWebServer
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ImageHttpServiceTest {

    private val mockWebServer = MockWebServer()
    private val okHttpClient = CatOkHttpClientBuilder("test").build()
    private val retrofit = CatRetrofitBuilder(okHttpClient).build()
    private val service = ImageHttpService(
        retrofit = retrofit,
        dispatcher = UnconfinedTestDispatcher()
    )

    @Test
    fun `image url is loaded from remote source`() = runTest {
        // given
        mockWebServer.enqueueResponse("image_response.json")

        // when
        val output = service.getImageUrl("0XYvRd7oD").getOrThrow()

        // then
        assertThat(output).isEqualTo("https://cdn2.thecatapi.com/images/0XYvRd7oD.jpg")
    }

}
