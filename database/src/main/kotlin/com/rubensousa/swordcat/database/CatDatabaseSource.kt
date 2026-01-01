package com.rubensousa.swordcat.database

import com.rubensousa.swordcat.database.internal.CatEntityMapper
import com.rubensousa.swordcat.domain.Cat
import com.rubensousa.swordcat.domain.CatLocalSource
import com.rubensousa.swordcat.domain.CatRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CatDatabaseSource internal constructor(
    database: CatDatabase,
    private val dispatcher: CoroutineDispatcher,
    private val entityMapper: CatEntityMapper,
) : CatLocalSource {

    private val catDao = database.catDao()

    constructor(database: CatDatabase, dispatcher: CoroutineDispatcher) : this(
        database, dispatcher, CatEntityMapper()
    )

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
                catDao.upsertCats(cats.map { cat -> entityMapper.mapToEntity(cat) })
            }
        }
    }

}
