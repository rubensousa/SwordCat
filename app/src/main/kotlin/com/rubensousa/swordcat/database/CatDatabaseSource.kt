package com.rubensousa.swordcat.database

import com.rubensousa.swordcat.database.internal.CatEntity
import com.rubensousa.swordcat.domain.Cat
import com.rubensousa.swordcat.domain.CatLocalSource
import com.rubensousa.swordcat.domain.CatRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CatDatabaseSource @Inject constructor(
    database: CatDatabase
) : CatLocalSource {

    private val catDao = database.catDao()

    override suspend fun loadCats(request: CatRequest): List<Cat> {
        return runCatching {
            catDao.getCats(
                limit = request.limit,
                offset = request.offset
            ).map { entity ->
                mapCatFromEntity(entity)
            }
        }.getOrNull().orEmpty()
    }

    override suspend fun saveCats(cats: List<Cat>) {
        runCatching {
            catDao.upsertCats(cats.map { cat -> mapCatToEntity(cat) })
        }
    }

    private fun mapCatFromEntity(entity: CatEntity): Cat {
        return Cat(
            id = entity.id,
            breedName = entity.breedName,
            origin = entity.origin,
            temperament = entity.temperament,
            description = entity.description,
            imageId = entity.imageId,
            lifespan = entity.lifespanStart..entity.lifespanEnd
        )
    }

    private fun mapCatToEntity(cat: Cat): CatEntity {
        return CatEntity(
            id = cat.id,
            breedName = cat.breedName,
            origin = cat.origin,
            temperament = cat.temperament,
            description = cat.description,
            imageId = cat.imageId,
            lifespanStart = cat.lifespan.first,
            lifespanEnd = cat.lifespan.last,
        )
    }

}