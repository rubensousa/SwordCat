package com.rubensousa.swordcat.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
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

    class Builder(private val context: Context) {

        fun build(): CatDatabase {
            return Room.databaseBuilder(
                context = context,
                name = "cat_db",
                klass = CatDatabase::class.java
            ).build()
        }
    }
}
