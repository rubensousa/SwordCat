package com.rubensousa.swordcat.domain

import kotlinx.coroutines.flow.Flow

interface CatRepository {
    suspend fun loadCats(request: CatRequest): Result<List<Cat>>
    suspend fun isFavorite(catId: String): Boolean
    suspend fun toggleFavorite(catId: String)
    fun observeFavoriteState(catId: String): Flow<Boolean>
}
