package com.rubensousa.swordcat.fixtures

import com.rubensousa.swordcat.domain.Cat
import com.rubensousa.swordcat.domain.CatRepository
import com.rubensousa.swordcat.domain.CatRequest

class FakeCatRepository : CatRepository {

    private var result = Result.failure<List<Cat>>(IllegalStateException("Not set"))
    private var catResult = Result.failure<Cat>(IllegalStateException("Not set"))
    private val cats = mutableMapOf<String, Cat>()

    override suspend fun loadCats(request: CatRequest): Result<List<Cat>> {
        return result
    }

    override suspend fun getCat(id: String): Result<Cat> {
        return catResult
    }

    fun setLoadCatsSuccessResult(cats: List<Cat>) {
        result = Result.success(cats)
    }

    fun setLoadCatsErrorResult(error: Throwable) {
        result = Result.failure(error)
    }

    fun setGetCatSuccessResult(cat: Cat) {
        catResult = Result.success(cat)
    }

    fun setGetCatErrorResult(error: Throwable) {
        catResult = Result.failure(error)
    }

}
