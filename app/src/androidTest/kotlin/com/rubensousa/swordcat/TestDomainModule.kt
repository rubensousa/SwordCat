package com.rubensousa.swordcat

import com.rubensousa.swordcat.domain.CatRepository
import com.rubensousa.swordcat.domain.DomainModule
import com.rubensousa.swordcat.domain.ImageRepository
import com.rubensousa.swordcat.domain.internal.ImageRepositoryImpl
import com.rubensousa.swordcat.fixtures.FakeCatRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DomainModule.Bindings::class]
)
@Module
class TestDomainModule {

    @Provides
    @Singleton
    fun provideFakeCatRepository(): FakeCatRepository {
        return FakeCatRepository()
    }

    @InstallIn(SingletonComponent::class)
    @Module
    abstract class Bindings {

        @Binds
        abstract fun bindCatRepository(impl: FakeCatRepository): CatRepository

        @Binds
        internal abstract fun bindImageRepository(impl: ImageRepositoryImpl): ImageRepository


    }

}
