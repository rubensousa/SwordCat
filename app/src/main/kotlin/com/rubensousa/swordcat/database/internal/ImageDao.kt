package com.rubensousa.swordcat.database.internal

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
internal interface ImageDao {

    @Upsert
    suspend fun upsertImage(entity: ImageEntity)

    @Query("SELECT * FROM image WHERE id = :id")
    suspend fun getImageEntity(id: String): ImageEntity?

}
