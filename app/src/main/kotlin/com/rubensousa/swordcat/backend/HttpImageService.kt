package com.rubensousa.swordcat.backend

import com.rubensousa.swordcat.backend.internal.CatService
import com.rubensousa.swordcat.domain.ImageRemoteService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HttpImageService @Inject internal constructor(
    private val catService: CatService,
    private val dispatcher: CoroutineDispatcher
) : ImageRemoteService {

    override suspend fun getImageUrl(imageId: String): Result<String> {
        return withContext(dispatcher) {
            runCatching {
                catService.getImage(imageId).url
            }
        }

    }

}
