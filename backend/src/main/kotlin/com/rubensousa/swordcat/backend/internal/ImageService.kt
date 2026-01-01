package com.rubensousa.swordcat.backend.internal

import retrofit2.http.GET
import retrofit2.http.Path

internal interface ImageService {

    @GET("images/{imageId}")
    suspend fun getImage(
        @Path("imageId") imageId: String,
    ): ImageRemoteModel

}
