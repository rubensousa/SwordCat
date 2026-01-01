package com.rubensousa.swordcat.database.internal

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "favorite_cat",
    primaryKeys = ["catId"],
    foreignKeys = [
        ForeignKey(
            entity = CatEntity::class,
            parentColumns = ["id"],
            childColumns = ["catId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
internal data class CatFavoriteEntity(
    @ColumnInfo("catId") val catId: String,
)
