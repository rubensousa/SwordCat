package com.rubensousa.swordcat.domain

import com.google.common.truth.Truth.assertThat
import com.rubensousa.swordcat.domain.fixtures.FakeImageCache
import com.rubensousa.swordcat.domain.fixtures.FakeImageRemoteService
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ImageRepositoryTest {

    private val imageCache = FakeImageCache()
    private val imageService = FakeImageRemoteService()
    private val repository = ImageRepositoryImpl(
        imageService = imageService,
        imageCache = imageCache
    )

    @Test
    fun `image url is loaded from remote service if cache is empty`() = runTest {
        // given
        val imageId = "id"
        val imageUrl = "https://example.com/image.jpg"
        imageService.setSuccess(imageUrl)

        // when
        val result = repository.getImageUrl(imageId).getOrThrow()

        // then
        assertThat(result).isEqualTo(imageUrl)
    }

    @Test
    fun `cache is updated after remote fetch`() = runTest {
        // given
        val imageId = "id"
        val imageUrl = "https://example.com/image.jpg"
        imageService.setSuccess(imageUrl)

        // when
        repository.getImageUrl(imageId).getOrThrow()

        // then
        assertThat(imageCache.getImageUrl(imageId)).isEqualTo(imageUrl)
    }

    @Test
    fun `image url is loaded from cache if available`() = runTest {
        // given
        val imageId = "id"
        val cachedUrl = "https://example.com/cached.jpg"
        imageCache.saveImageUrl(imageId, cachedUrl)

        // when
        val result = repository.getImageUrl(imageId).getOrThrow()

        // then
        assertThat(result).isEqualTo(cachedUrl)
    }

    @Test
    fun `error from remote service is returned to consumer`() = runTest {
        // given
        val imageId = "id"
        val errorCause = IllegalStateException("Whoops")
        imageService.setError(errorCause)

        // when
        val result = repository.getImageUrl(imageId).exceptionOrNull()

        // then
        assertThat(result).isEqualTo(errorCause)
    }

}
