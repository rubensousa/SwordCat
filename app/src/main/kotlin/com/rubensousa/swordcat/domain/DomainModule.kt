package com.rubensousa.swordcat.domain

import com.rubensousa.swordcat.domain.internal.CatRepositoryImpl
import com.rubensousa.swordcat.domain.internal.ImageRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class DomainModule {

    @InstallIn(SingletonComponent::class)
    @Module
    abstract class Bindings {

        @Binds
        internal abstract fun bindCatRepository(impl: CatRepositoryImpl): CatRepository

        @Binds
        internal abstract fun bindImageRepository(impl: ImageRepositoryImpl): ImageRepository
    }

}
