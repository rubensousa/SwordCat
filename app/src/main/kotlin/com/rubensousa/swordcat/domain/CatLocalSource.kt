package com.rubensousa.swordcat.domain

import kotlinx.coroutines.flow.Flow

interface CatLocalSource {
    suspend fun loadCats(request: CatRequest): List<Cat>
    suspend fun getCat(id: String): Cat?
    suspend fun saveCats(cats: List<Cat>)
    suspend fun setFavoriteCat(catId: String, isFavorite: Boolean)
    fun observeIsFavorite(catId: String): Flow<Boolean>
}
