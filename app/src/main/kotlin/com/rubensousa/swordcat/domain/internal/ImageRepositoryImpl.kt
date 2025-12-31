package com.rubensousa.swordcat.domain.internal

import com.rubensousa.swordcat.domain.ImageCache
import com.rubensousa.swordcat.domain.ImageRepository
import com.rubensousa.swordcat.domain.ImageRemoteService
import javax.inject.Inject

internal class ImageRepositoryImpl @Inject constructor(
    private val imageService: ImageRemoteService,
    private val imageCache: ImageCache,
) : ImageRepository {

    override suspend fun getImageUrl(imageId: String): Result<String> {
        val localImageUrl = imageCache.getImageUrl(imageId)
        if (localImageUrl == null) {
            return imageService.getImageUrl(imageId)
                .onSuccess { url ->
                    imageCache.saveImageUrl(
                        imageId = imageId,
                        imageUrl = url
                    )
                }
        }
        return Result.success(localImageUrl)
    }

}
