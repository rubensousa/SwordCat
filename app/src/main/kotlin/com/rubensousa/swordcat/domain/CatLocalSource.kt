package com.rubensousa.swordcat.domain

interface CatLocalSource {
    suspend fun loadCats(request: CatRequest): List<Cat>
    suspend fun saveCats(cats: List<Cat>)
}
