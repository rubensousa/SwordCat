package com.rubensousa.swordcat.fixtures

import com.rubensousa.swordcat.domain.Cat
import com.rubensousa.swordcat.domain.CatRepository
import com.rubensousa.swordcat.domain.CatRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull

class FakeCatRepository : CatRepository {

    private val result = MutableStateFlow<Result<List<Cat>>?>(null)
    private var catResult = Result.failure<Cat>(IllegalStateException("Not set"))

    override fun loadCats(request: CatRequest): Flow<Result<List<Cat>>> {
        return result.filterNotNull()
    }

    override suspend fun getCat(id: String): Result<Cat> {
        return catResult
    }

    fun setLoadCatsSuccessResult(cats: List<Cat>) {
        result.value = Result.success(cats)
    }

    fun setLoadCatsErrorResult(error: Throwable) {
        result.value = Result.failure(error)
    }

    fun setGetCatSuccessResult(cat: Cat) {
        catResult = Result.success(cat)
    }

    fun setGetCatErrorResult(error: Throwable) {
        catResult = Result.failure(error)
    }

}
