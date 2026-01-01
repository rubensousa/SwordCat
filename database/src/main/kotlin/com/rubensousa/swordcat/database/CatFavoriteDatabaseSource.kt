package com.rubensousa.swordcat.database

import com.rubensousa.swordcat.database.internal.CatEntityMapper
import com.rubensousa.swordcat.database.internal.CatFavoriteEntity
import com.rubensousa.swordcat.domain.Cat
import com.rubensousa.swordcat.domain.CatFavoriteLocalSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class CatFavoriteDatabaseSource internal constructor(
    database: CatDatabase,
    private val dispatcher: CoroutineDispatcher,
    private val entityMapper: CatEntityMapper
) : CatFavoriteLocalSource {

    private val catDao = database.catDao()

    constructor(database: CatDatabase, dispatcher: CoroutineDispatcher) : this(
        database, dispatcher, CatEntityMapper()
    )

    override fun observeIsFavorite(catId: String): Flow<Boolean> {
        return catDao.observeFavorite(catId)
            .map { list -> list.isNotEmpty() }
            .flowOn(dispatcher)
    }

    override suspend fun isFavorite(catId: String): Boolean {
        return withContext(dispatcher) {
            observeIsFavorite(catId).first()
        }
    }

    override fun observeFavoriteCats(): Flow<List<Cat>> {
        return catDao.observeFavoriteCats().map { list ->
            list.map { catEntity ->
                entityMapper.mapFromEntity(catEntity)
            }
        }.flowOn(dispatcher)
    }

    override suspend fun toggleFavorite(catId: String) {
        withContext(dispatcher) {
            val isFavorite = isFavorite(catId)
            setFavoriteCat(catId, !isFavorite)
        }
    }

    private suspend fun setFavoriteCat(catId: String, isFavorite: Boolean) {
        withContext(dispatcher) {
            runCatching {
                if (isFavorite) {
                    catDao.setFavorite(CatFavoriteEntity(catId = catId))
                } else {
                    catDao.deleteFavorite(catId)
                }
            }
        }
    }

}
