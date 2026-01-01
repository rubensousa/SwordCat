package com.rubensousa.swordcat.database

import com.rubensousa.swordcat.database.internal.ImageEntity
import com.rubensousa.swordcat.domain.ImageCache
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ImageDatabaseCache(
    database: CatDatabase,
    private val dispatcher: CoroutineDispatcher
) : ImageCache {

    private val dao = database.imageDao()

    override suspend fun getImageUrl(imageId: String): String? {
        return withContext(dispatcher) {
            runCatching {
                dao.getImageEntity(imageId)?.url
            }.getOrNull()
        }
    }

    override suspend fun saveImageUrl(imageId: String, imageUrl: String) {
        withContext(dispatcher) {
            runCatching {
                dao.upsertImage(ImageEntity(id = imageId, url = imageUrl))
            }
        }
    }
}