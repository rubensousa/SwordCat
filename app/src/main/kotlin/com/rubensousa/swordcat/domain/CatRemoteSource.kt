package com.rubensousa.swordcat.domain

interface CatRemoteSource {
    suspend fun loadCats(request: CatRequest): Result<List<Cat>>
}
