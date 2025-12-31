package com.rubensousa.swordcat.fixtures

import com.rubensousa.swordcat.domain.ImageRemoteService

class FakeImageRemoteService : ImageRemoteService {

    private var result = Result.failure<String>(IllegalStateException("Not set"))

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
