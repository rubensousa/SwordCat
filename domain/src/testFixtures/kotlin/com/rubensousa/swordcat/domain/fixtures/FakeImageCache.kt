package com.rubensousa.swordcat.domain.fixtures

import com.rubensousa.swordcat.domain.ImageCache

class FakeImageCache : ImageCache {

    private val cache = mutableMapOf<String, String>()

    override suspend fun getImageUrl(imageId: String): String? {
        return cache[imageId]
    }

    override suspend fun saveImageUrl(imageId: String, imageUrl: String) {
        cache[imageId] = imageUrl
    }
}
