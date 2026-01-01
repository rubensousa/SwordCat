package com.rubensousa.swordcat.app

import android.content.Context
import com.rubensousa.swordcat.database.CatDatabase
import com.rubensousa.swordcat.database.CatDatabaseSource
import com.rubensousa.swordcat.database.CatFavoriteDatabaseSource
import com.rubensousa.swordcat.database.ImageDatabaseCache
import com.rubensousa.swordcat.domain.CatFavoriteLocalSource
import com.rubensousa.swordcat.domain.CatLocalSource
import com.rubensousa.swordcat.domain.ImageCache
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): CatDatabase {
        return CatDatabase.Builder(context).build()
    }

    @Provides
    @Singleton
    fun provideCatDatabaseSource(
        database: CatDatabase,
        dispatcher: CoroutineDispatcher,
    ): CatDatabaseSource {
        return CatDatabaseSource(
            database = database,
            dispatcher = dispatcher
        )
    }

    @Provides
    @Singleton
    fun provideCatFavoriteDatabaseSource(
        database: CatDatabase,
        dispatcher: CoroutineDispatcher,
    ): CatFavoriteDatabaseSource {
        return CatFavoriteDatabaseSource(
            database = database,
            dispatcher = dispatcher
        )
    }

    @Provides
    @Singleton
    fun provideImageDatabaseCache(
        database: CatDatabase,
        dispatcher: CoroutineDispatcher,
    ): ImageDatabaseCache {
        return ImageDatabaseCache(
            database = database,
            dispatcher = dispatcher
        )
    }

    @InstallIn(SingletonComponent::class)
    @Module
    abstract class Bindings {

        @Binds
        abstract fun bindCatLocalSource(impl: CatDatabaseSource): CatLocalSource

        @Binds
        abstract fun bindCatFavoriteLocalSource(
            impl: CatFavoriteDatabaseSource
        ): CatFavoriteLocalSource

        @Binds
        abstract fun bindImageCache(impl: ImageDatabaseCache): ImageCache

    }

}
