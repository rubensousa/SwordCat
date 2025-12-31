package com.rubensousa.swordcat.database

import com.rubensousa.swordcat.database.internal.CatEntity
import com.rubensousa.swordcat.database.internal.CatFavoriteEntity
import com.rubensousa.swordcat.domain.Cat
import com.rubensousa.swordcat.domain.CatLocalSource
import com.rubensousa.swordcat.domain.CatRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CatDatabaseSource @Inject constructor(
    database: CatDatabase,
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
                    mapCatFromEntity(entity)
                }
            }.getOrNull().orEmpty()
        }
    }

    override suspend fun getCat(id: String): Cat? {
        return withContext(dispatcher) {
            runCatching {
                catDao.getCat(id)?.let { mapCatFromEntity(it) }
            }.getOrNull()
        }
    }

    override fun observeIsFavorite(catId: String): Flow<Boolean> {
        return catDao.observeFavorite(catId)
            .map { list -> list.isNotEmpty() }
            .flowOn(dispatcher)
    }

    override suspend fun setFavoriteCat(catId: String, isFavorite: Boolean) {
        withContext(dispatcher) {
            runCatching {
                if (isFavorite) {
                    catDao.setFavorite(
                        CatFavoriteEntity(
                            catId = catId
                        )
                    )
                } else {
                    catDao.deleteFavorite(catId)
                }

            }
        }
    }

    override suspend fun saveCats(cats: List<Cat>) {
        withContext(dispatcher) {
            runCatching {
                catDao.upsertCats(cats.map { cat -> mapCatToEntity(cat) })
            }
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
