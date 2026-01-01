package com.rubensousa.swordcat.database.internal

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "cat",
    primaryKeys = ["id"]
)
internal data class CatEntity(
    @ColumnInfo("id") val id: String,
    @ColumnInfo("breed_name") val breedName: String,
    @ColumnInfo("origin") val origin: String,
    @ColumnInfo("temperament") val temperament: String,
    @ColumnInfo("description") val description: String,
    @ColumnInfo("imageId") val imageId: String?,
    @ColumnInfo("lifespan_start") val lifespanStart: Int,
    @ColumnInfo("lifespan_end") val lifespanEnd: Int,
)