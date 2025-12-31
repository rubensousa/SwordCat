package com.rubensousa.swordcat

import com.rubensousa.swordcat.backend.BackendModule
import com.rubensousa.swordcat.domain.CatRemoteSource
import com.rubensousa.swordcat.domain.ImageRemoteService
import com.rubensousa.swordcat.fixtures.FakeCatRemoteSource
import com.rubensousa.swordcat.fixtures.FakeImageRemoteService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [BackendModule.Bindings::class]
)
@Module
class TestBackendModule {

    @Provides
    @Singleton
    fun provideFakeCatRemoteSource(): FakeCatRemoteSource {
        return FakeCatRemoteSource()
    }

    @Provides
    @Singleton
    fun provideFakeImageService(): FakeImageRemoteService {
        return FakeImageRemoteService()
    }

    @InstallIn(SingletonComponent::class)
    @Module
    abstract class Bindings {

        @Binds
        abstract fun bindCatRemoteSource(impl: FakeCatRemoteSource): CatRemoteSource

        @Binds
        abstract fun bindImageService(impl: FakeImageRemoteService): ImageRemoteService
    }

}
