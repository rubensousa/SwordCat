package com.rubensousa.swordcat.app.list

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ListModule {

    @Provides
    @Singleton
    fun provideListConfig(): ListConfig {
        return ListConfig(searchDebounceMs = 350L)
    }
}