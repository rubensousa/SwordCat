package com.rubensousa.swordcat.database.internal

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "image",
    primaryKeys = ["id"]
)
internal data class ImageEntity(
    @ColumnInfo("id") val id: String,
    @ColumnInfo("url") val url: String,
)
