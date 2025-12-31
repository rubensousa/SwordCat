package com.rubensousa.swordcat.fixtures

import com.rubensousa.swordcat.domain.Cat
import com.rubensousa.swordcat.domain.CatLocalSource
import com.rubensousa.swordcat.domain.CatRequest

class FakeCatLocalSource : CatLocalSource {

    private val catCache = mutableMapOf<String, Cat>()

    override suspend fun loadCats(request: CatRequest): List<Cat> {
        return catCache.values.sortedBy { it.breedName }
    }

    override suspend fun getCat(id: String): Cat? {
        return catCache[id]
    }

    override suspend fun saveCats(cats: List<Cat>) {
        cats.forEach { cat ->
            catCache[cat.id] = cat
        }
    }
}
