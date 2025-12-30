package com.rubensousa.swordcat.database

import android.content.Context
import androidx.room.Room

class DatabaseBuilder(
    private val context: Context
) {

    fun build(): CatDatabase {
        return Room.databaseBuilder(
            context = context,
            name = "cat_db",
            klass = CatDatabase::class.java
        ).build()
    }

}
