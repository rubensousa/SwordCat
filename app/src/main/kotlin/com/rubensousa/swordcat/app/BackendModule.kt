package com.rubensousa.swordcat.app

import com.rubensousa.swordcat.backend.CatHttpService
import com.rubensousa.swordcat.backend.CatOkHttpClientBuilder
import com.rubensousa.swordcat.backend.CatRetrofitBuilder
import com.rubensousa.swordcat.backend.ImageHttpService
import com.rubensousa.swordcat.domain.CatRemoteSource
import com.rubensousa.swordcat.domain.ImageRemoteService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

private const val API_KEY = "live_hq1UFxUSjbMx7tokbsRAKbESVLmb1PYB9wOFZw4ZFAX1HLyvikmkT5n8fuqdLxRc"

@InstallIn(SingletonComponent::class)
@Module
class BackendModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return CatOkHttpClientBuilder(API_KEY).build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return CatRetrofitBuilder(okHttpClient).build()
    }

    @Provides
    @Singleton
    fun provideCatHttpService(
        retrofit: Retrofit,
        dispatcher: CoroutineDispatcher,
    ): CatHttpService {
        return CatHttpService(
            retrofit = retrofit,
            dispatcher = dispatcher
        )
    }

    @Provides
    @Singleton
    fun provideImageHttpService(
        retrofit: Retrofit,
        dispatcher: CoroutineDispatcher,
    ): ImageHttpService {
        return ImageHttpService(
            retrofit = retrofit,
            dispatcher = dispatcher
        )
    }

    @InstallIn(SingletonComponent::class)
    @Module
    abstract class Bindings {

        @Binds
        abstract fun bindCatRemoteSource(impl: CatHttpService): CatRemoteSource

        @Binds
        abstract fun bindImageService(impl: ImageHttpService): ImageRemoteService

    }

}
