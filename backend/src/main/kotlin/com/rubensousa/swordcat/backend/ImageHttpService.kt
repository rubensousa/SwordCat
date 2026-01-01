package com.rubensousa.swordcat.backend

import com.rubensousa.swordcat.backend.internal.ImageService
import com.rubensousa.swordcat.domain.ImageRemoteService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.create

class ImageHttpService(
    retrofit: Retrofit,
    private val dispatcher: CoroutineDispatcher
) : ImageRemoteService {

    private val imageService = retrofit.create<ImageService>()

    override suspend fun getImageUrl(imageId: String): Result<String> {
        return withContext(dispatcher) {
            runCatching {
                imageService.getImage(imageId).url
            }
        }
    }

}
