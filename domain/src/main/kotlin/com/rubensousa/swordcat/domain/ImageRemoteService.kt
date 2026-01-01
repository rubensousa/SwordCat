package com.rubensousa.swordcat.domain

interface ImageRemoteService {
    suspend fun getImageUrl(imageId: String): Result<String>
}
