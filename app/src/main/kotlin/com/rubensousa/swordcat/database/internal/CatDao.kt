package com.rubensousa.swordcat.database.internal

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
internal interface CatDao {

    @Upsert
    suspend fun upsertCats(entities: List<CatEntity>)

    @Query("SELECT * FROM cat ORDER BY breed_name ASC LIMIT :limit OFFSET :offset")
    suspend fun getCats(offset: Int, limit: Int): List<CatEntity>

}
