package com.rubensousa.swordcat.fixtures

import com.rubensousa.swordcat.domain.Cat
import com.rubensousa.swordcat.domain.CatRemoteSource
import com.rubensousa.swordcat.domain.CatRequest

class FakeCatRemoteSource : CatRemoteSource {

    private var catResult = Result.failure<List<Cat>>(IllegalStateException("Not set"))

    override suspend fun loadCats(request: CatRequest): Result<List<Cat>> {
        return catResult
    }

    fun setLoadCatSuccessResult(cats: List<Cat>) {
        catResult = Result.success(cats)
    }

    fun setLoadCatErrorResult(error: Throwable) {
        catResult = Result.failure(error)
    }

}
