package com.rubensousa.swordcat.domain.internal

import com.rubensousa.swordcat.domain.Cat
import com.rubensousa.swordcat.domain.CatLocalSource
import com.rubensousa.swordcat.domain.CatRemoteSource
import com.rubensousa.swordcat.domain.CatRepository
import com.rubensousa.swordcat.domain.CatRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class CatRepositoryImpl @Inject constructor(
    private val localSource: CatLocalSource,
    private val remoteSource: CatRemoteSource,
) : CatRepository {

    override suspend fun loadCats(request: CatRequest): Result<List<Cat>> {
        /**
         * For simplicity of state management,
         * opted for network first and local as a fallback only.
         * Otherwise, using local first with offset/limit pagination would be out of sync
         */
        return remoteSource.loadCats(request)
            .fold(
                onSuccess = { remoteContent ->
                    localSource.saveCats(remoteContent)
                    Result.success(remoteContent)
                },
                onFailure = { error ->
                    val localContent = localSource.loadCats(request)
                    if (localContent.isEmpty()) {
                        Result.failure(error)
                    } else {
                        Result.success(localContent)
                    }
                }
            )
    }

}

