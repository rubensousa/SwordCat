package com.rubensousa.swordcat.app.image

import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.util.DebugLogger
import com.rubensousa.swordcat.BuildConfig
import com.rubensousa.swordcat.domain.ImageRepository
import okhttp3.OkHttpClient
import javax.inject.Inject

class CoilInitializer @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val imageRepository: ImageRepository
) {

    fun init() {
        SingletonImageLoader.setSafe { context ->
            ImageLoader.Builder(context)
                .components {
                    val networkFetcherFactory = OkHttpNetworkFetcherFactory(
                        callFactory = { okHttpClient }
                    )
                    add(ImageReferenceKeyer())
                    add(
                        ImageReferenceFetcher.Factory(
                            imageRepository = imageRepository,
                            networkFetcherFactory = networkFetcherFactory
                        )
                    )
                    add(networkFetcherFactory)
                }
                .logger(if (BuildConfig.DEBUG) DebugLogger() else null)
                .build()
        }
    }

}
