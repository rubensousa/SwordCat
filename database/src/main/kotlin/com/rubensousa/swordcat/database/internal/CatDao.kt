package com.rubensousa.swordcat.database.internal

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
internal interface CatDao {

    @Upsert
    suspend fun upsertCats(entities: List<CatEntity>)

    @Query("SELECT * FROM cat ORDER BY breed_name ASC LIMIT :limit OFFSET :offset")
    suspend fun getCats(offset: Int, limit: Int): List<CatEntity>

    @Query("SELECT * FROM cat WHERE id = :id")
    suspend fun getCat(id: String): CatEntity?

    @Query("SELECT * FROM favorite_cat WHERE catId = :catId")
    fun observeFavorite(catId: String): Flow<List<CatFavoriteEntity>>

    @Query("SELECT cat.* FROM cat INNER JOIN favorite_cat ON cat.id = favorite_cat.catId ORDER BY cat.breed_name ASC")
    fun observeFavoriteCats(): Flow<List<CatEntity>>

    @Upsert
    suspend fun setFavorite(entity: CatFavoriteEntity)

    @Query("DELETE FROM favorite_cat WHERE catId = :catId")
    suspend fun deleteFavorite(catId: String)
}