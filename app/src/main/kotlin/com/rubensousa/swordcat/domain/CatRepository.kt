package com.rubensousa.swordcat.domain

interface CatRepository {
    suspend fun loadCats(request: CatRequest): Result<List<Cat>>
    suspend fun getCat(id: String): Result<Cat>
}
