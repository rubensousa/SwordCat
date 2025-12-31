package com.rubensousa.swordcat.database

import com.rubensousa.swordcat.database.internal.CatEntity
import com.rubensousa.swordcat.database.internal.CatEntityMapper
import com.rubensousa.swordcat.domain.Cat
import com.rubensousa.swordcat.domain.CatLocalSource
import com.rubensousa.swordcat.domain.CatRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CatDatabaseSource @Inject internal constructor(
    database: CatDatabase,
    private val entityMapper: CatEntityMapper,
    private val dispatcher: CoroutineDispatcher
) : CatLocalSource {

    private val catDao = database.catDao()

    override suspend fun loadCats(request: CatRequest): List<Cat> {
        return withContext(dispatcher) {
            runCatching {
                catDao.getCats(
                    limit = request.limit,
                    offset = request.offset
                ).map { entity ->
                    entityMapper.mapFromEntity(entity)
                }
            }.getOrNull().orEmpty()
        }
    }

    override suspend fun getCat(id: String): Cat? {
        return withContext(dispatcher) {
            runCatching {
                catDao.getCat(id)?.let { entity -> entityMapper.mapFromEntity(entity) }
            }.getOrNull()
        }
    }

    override suspend fun saveCats(cats: List<Cat>) {
        withContext(dispatcher) {
            runCatching {
                catDao.upsertCats(cats.map { cat -> mapCatToEntity(cat) })
            }
        }
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
