package com.rubensousa.swordcat.domain

interface ImageRepository {
   suspend fun getImageUrl(imageId: String): Result<String>
}
