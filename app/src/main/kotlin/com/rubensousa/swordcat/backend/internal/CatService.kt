package com.rubensousa.swordcat.backend.internal

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface CatService {

    @GET("breeds")
    suspend fun getBreeds(
        @Query("limit") limit: Int,
        @Query("page") page: Int
    ): List<CatRemoteModel>

    @GET("images/{imageId}")
    suspend fun getImage(
        @Path("imageId") imageId: String,
    ): ImageRemoteModel

}
