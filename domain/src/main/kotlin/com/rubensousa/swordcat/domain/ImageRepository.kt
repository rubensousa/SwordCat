package com.rubensousa.swordcat.domain

interface ImageRepository {
    suspend fun getImageUrl(imageId: String): Result<String>
}

class ImageRepositoryImpl(
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
