package com.rubensousa.swordcat.ui.image

import coil3.ImageLoader
import coil3.Uri
import coil3.annotation.ExperimentalCoilApi
import coil3.fetch.FetchResult
import coil3.fetch.Fetcher
import coil3.network.NetworkFetcher
import coil3.request.Options
import com.rubensousa.swordcat.domain.ImageRepository

class ImageReferenceFetcher(
    private val data: ImageReference,
    private val options: Options,
    private val imageLoader: ImageLoader,
    private val imageRepository: ImageRepository,
    private val networkFetcherFactory: NetworkFetcher.Factory,
) : Fetcher {

    @OptIn(ExperimentalCoilApi::class)
    override suspend fun fetch(): FetchResult? {
        val image = imageRepository.getImageUrl(data.id).getOrNull() ?: return null
        val networkFetcher = networkFetcherFactory.create(
            data = Uri(
                path = image.replace("https://", ""),
                scheme = "https"
            ),
            options = options,
            imageLoader = imageLoader,
        )
        return networkFetcher?.fetch()
    }

    class Factory(
        private val networkFetcherFactory: NetworkFetcher.Factory,
        private val imageRepository: ImageRepository,
    ) : Fetcher.Factory<ImageReference> {
        override fun create(
            data: ImageReference,
            options: Options,
            imageLoader: ImageLoader
        ): Fetcher {
            return ImageReferenceFetcher(
                data = data,
                options = options,
                imageLoader = imageLoader,
                imageRepository = imageRepository,
                networkFetcherFactory = networkFetcherFactory
            )
        }
    }

}

