package com.rubensousa.swordcat.app

import com.rubensousa.swordcat.domain.CatLocalSource
import com.rubensousa.swordcat.domain.CatRemoteSource
import com.rubensousa.swordcat.domain.CatRepository
import com.rubensousa.swordcat.domain.CatRepositoryImpl
import com.rubensousa.swordcat.domain.ImageCache
import com.rubensousa.swordcat.domain.ImageRemoteService
import com.rubensousa.swordcat.domain.ImageRepository
import com.rubensousa.swordcat.domain.ImageRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DomainModule {

    @Provides
    @Singleton
    fun provideCatRepository(
        localSource: CatLocalSource,
        remoteSource: CatRemoteSource,
    ): CatRepositoryImpl {
        return CatRepositoryImpl(
            localSource = localSource,
            remoteSource = remoteSource
        )
    }

    @Provides
    @Singleton
    fun provideImageRepository(
        imageCache: ImageCache,
        imageService: ImageRemoteService,
    ): ImageRepositoryImpl {
        return ImageRepositoryImpl(
            imageCache = imageCache,
            imageService = imageService
        )
    }

    @InstallIn(SingletonComponent::class)
    @Module
    abstract class Bindings {

        @Binds
        internal abstract fun bindCatRepository(impl: CatRepositoryImpl): CatRepository

        @Binds
        internal abstract fun bindImageRepository(impl: ImageRepositoryImpl): ImageRepository
    }

}
