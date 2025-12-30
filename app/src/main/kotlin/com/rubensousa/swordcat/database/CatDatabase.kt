package com.rubensousa.swordcat.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rubensousa.swordcat.database.internal.CatDao
import com.rubensousa.swordcat.database.internal.CatEntity

@Database(
    entities = [
        CatEntity::class
    ],
    version = 1
)
abstract class CatDatabase : RoomDatabase() {

    internal abstract fun catDao(): CatDao

}

