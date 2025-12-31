package com.rubensousa.swordcat.fixtures

import com.rubensousa.swordcat.domain.Cat
import com.rubensousa.swordcat.domain.CatFavoriteLocalSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeCatFavoriteLocalSource : CatFavoriteLocalSource {

    private val favoriteCats = MutableStateFlow<List<Cat>>(emptyList())
    private val favoriteFlows = mutableMapOf<String, MutableStateFlow<Boolean>>()
    private val availableCats = mutableMapOf<String, Cat>()

    override suspend fun isFavorite(catId: String): Boolean {
        return favoriteFlows[catId]?.value == true
    }

    override suspend fun toggleFavorite(catId: String) {
        val state = favoriteFlows.getOrPut(catId) { MutableStateFlow(false) }
        val isFavorite = !state.value
        state.value = isFavorite
        val newList = favoriteCats.value.toMutableList()
        if (isFavorite) {
            availableCats[catId]?.let { cat ->
                newList.add(cat)
            }
        } else {
            newList.removeIf { cat -> cat.id == catId }
        }
        favoriteCats.value = newList
    }

    override fun observeIsFavorite(catId: String): Flow<Boolean> {
        return favoriteFlows.getOrPut(catId) { MutableStateFlow(false) }
    }

    override fun observeFavoriteCats(): Flow<List<Cat>> {
        return favoriteCats.map { list -> list.sortedBy { cat -> cat.breedName } }
    }

    fun setAvailableCats(cats: List<Cat>) {
        cats.forEach { cat ->
            availableCats[cat.id] = cat
        }
    }

}
