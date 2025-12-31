package com.rubensousa.swordcat.fixtures

import com.rubensousa.swordcat.domain.Cat
import com.rubensousa.swordcat.domain.CatRepository
import com.rubensousa.swordcat.domain.CatRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeCatRepository : CatRepository {

    private var result = Result.failure<List<Cat>>(IllegalStateException("Not set"))
    private val favoriteState = MutableStateFlow<Map<String, Boolean>>(emptyMap())

    override suspend fun loadCats(request: CatRequest): Result<List<Cat>> {
        return result
    }

    override suspend fun isFavorite(catId: String): Boolean {
        return favoriteState.value[catId] ?: false
    }

    override suspend fun toggleFavorite(catId: String) {
        favoriteState.value = favoriteState.value.toMutableMap().apply {
            put(catId, !isFavorite(catId))
        }
    }

    override fun observeFavoriteState(catId: String): Flow<Boolean> {
        return favoriteState.map { it[catId] ?: false }
    }

    fun setLoadCatsSuccessResult(cats: List<Cat>) {
        result = Result.success(cats)
    }

    fun setLoadCatsErrorResult(error: Throwable) {
        result = Result.failure(error)
    }

}
