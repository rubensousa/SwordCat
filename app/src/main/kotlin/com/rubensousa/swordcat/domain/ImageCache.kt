package com.rubensousa.swordcat.domain

interface ImageCache {
    suspend fun getImageUrl(imageId: String): String?
    suspend fun saveImageUrl(imageId: String, imageUrl: String)
}
