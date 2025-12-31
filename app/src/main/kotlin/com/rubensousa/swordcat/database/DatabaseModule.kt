package com.rubensousa.swordcat.database

import android.content.Context
import com.rubensousa.swordcat.domain.CatFavoriteLocalSource
import com.rubensousa.swordcat.domain.CatLocalSource
import com.rubensousa.swordcat.domain.ImageCache
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): CatDatabase {
        return DatabaseBuilder(context).build()
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
