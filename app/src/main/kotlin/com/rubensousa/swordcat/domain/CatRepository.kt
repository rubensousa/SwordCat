package com.rubensousa.swordcat.domain

import kotlinx.coroutines.flow.Flow

interface CatRepository {
    fun loadCats(request: CatRequest): Flow<Result<List<Cat>>>
    suspend fun getCat(id: String): Result<Cat>
}
