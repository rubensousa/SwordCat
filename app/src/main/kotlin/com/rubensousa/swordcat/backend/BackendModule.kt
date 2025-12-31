package com.rubensousa.swordcat.backend

import com.rubensousa.swordcat.backend.internal.AuthenticationInterceptor
import com.rubensousa.swordcat.backend.internal.CatService
import com.rubensousa.swordcat.backend.internal.RetrofitBuilder
import com.rubensousa.swordcat.domain.CatRemoteSource
import com.rubensousa.swordcat.domain.ImageRemoteService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class BackendModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthenticationInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return RetrofitBuilder(okHttpClient).build()
    }

    @Provides
    @Singleton
    internal fun provideCatService(
        retrofit: Retrofit,
    ): CatService {
        return retrofit.create()
    }

    @InstallIn(SingletonComponent::class)
    @Module
    abstract class Bindings {

        @Binds
        abstract fun bindCatRemoteSource(impl: CatHttpSource): CatRemoteSource

        @Binds
        abstract fun bindImageService(impl: HttpImageService): ImageRemoteService

    }

}
