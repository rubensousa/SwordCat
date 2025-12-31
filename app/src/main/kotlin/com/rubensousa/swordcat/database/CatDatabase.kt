package com.rubensousa.swordcat.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rubensousa.swordcat.database.internal.CatDao
import com.rubensousa.swordcat.database.internal.CatEntity
import com.rubensousa.swordcat.database.internal.CatFavoriteEntity
import com.rubensousa.swordcat.database.internal.ImageDao
import com.rubensousa.swordcat.database.internal.ImageEntity

@Database(
    entities = [
        CatEntity::class,
        CatFavoriteEntity::class,
        ImageEntity::class,
    ],
    version = 1,
)
abstract class CatDatabase : RoomDatabase() {

    internal abstract fun catDao(): CatDao
    internal abstract fun imageDao(): ImageDao

}
