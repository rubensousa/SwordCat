package com.rubensousa.swordcat.fixtures

import com.rubensousa.swordcat.domain.Cat
import com.rubensousa.swordcat.domain.CatLocalSource
import com.rubensousa.swordcat.domain.CatRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeCatLocalSource : CatLocalSource {

    private val catCache = mutableMapOf<String, Cat>()
    private val favoriteState = MutableStateFlow<Map<String, Boolean>>(emptyMap())

    override suspend fun loadCats(request: CatRequest): List<Cat> {
        return catCache.values.sortedBy { it.breedName }
    }

    override suspend fun saveCats(cats: List<Cat>) {
        cats.forEach { cat ->
            catCache[cat.id] = cat
        }
    }

    override suspend fun setFavoriteCat(catId: String, isFavorite: Boolean) {
        favoriteState.value = favoriteState.value.toMutableMap().apply {
            put(catId, isFavorite)
        }
    }

    override fun observeIsFavorite(catId: String): Flow<Boolean> {
        return favoriteState.map { it[catId] ?: false }
    }
}
