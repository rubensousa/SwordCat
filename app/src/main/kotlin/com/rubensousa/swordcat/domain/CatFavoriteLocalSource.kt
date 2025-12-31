package com.rubensousa.swordcat.domain

import kotlinx.coroutines.flow.Flow

interface CatFavoriteLocalSource {
    suspend fun isFavorite(catId: String): Boolean
    suspend fun toggleFavorite(catId: String)
    fun observeIsFavorite(catId: String): Flow<Boolean>
    fun observeFavoriteCats(): Flow<List<Cat>>
}
