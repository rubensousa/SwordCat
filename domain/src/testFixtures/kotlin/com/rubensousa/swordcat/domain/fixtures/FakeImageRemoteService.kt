package com.rubensousa.swordcat.domain.fixtures

import com.rubensousa.swordcat.domain.ImageRemoteService

class FakeImageRemoteService : ImageRemoteService {

    private var result = Result.success("https://cdn2.thecatapi.com/images/0XYvRd7oD.jpg")

    override suspend fun getImageUrl(imageId: String): Result<String> {
        return result
    }

    fun setSuccess(url: String) {
        result = Result.success(url)
    }

    fun setError(error: Throwable) {
        result = Result.failure(error)
    }

}
