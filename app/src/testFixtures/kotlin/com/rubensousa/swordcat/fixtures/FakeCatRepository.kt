package com.rubensousa.swordcat.fixtures

import com.rubensousa.swordcat.domain.Cat
import com.rubensousa.swordcat.domain.CatRepository
import com.rubensousa.swordcat.domain.CatRequest

class FakeCatRepository : CatRepository {

    private var result = Result.failure<List<Cat>>(IllegalStateException("Not set"))

    override suspend fun loadCats(request: CatRequest): Result<List<Cat>> {
        return result
    }

    fun setLoadCatsSuccessResult(cats: List<Cat>) {
        result = Result.success(cats)
    }

    fun setLoadCatsErrorResult(error: Throwable) {
        result = Result.failure(error)
    }

}
