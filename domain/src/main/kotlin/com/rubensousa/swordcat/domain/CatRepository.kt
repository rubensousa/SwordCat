package com.rubensousa.swordcat.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow

interface CatRepository {
    fun loadCats(request: CatRequest): Flow<Result<List<Cat>>>
    suspend fun getCat(id: String): Result<Cat>
}

class CatRepositoryImpl(
    private val localSource: CatLocalSource,
    private val remoteSource: CatRemoteSource,
) : CatRepository {

    override fun loadCats(request: CatRequest): Flow<Result<List<Cat>>> = flow {
        val localContent = localSource.loadCats(request)
        if (localContent.isNotEmpty()) {
            emit(Result.success(localContent))
        }
        val remoteResult = remoteSource.loadCats(request)
            .fold(
                onSuccess = { remoteContent ->
                    localSource.saveCats(remoteContent)
                    Result.success(remoteContent)
                },
                onFailure = { error ->
                    if (localContent.isEmpty()) {
                        Result.failure(error)
                    } else {
                        Result.success(localContent)
                    }
                }
            )
        emit(remoteResult)
    }.distinctUntilChanged()

    override suspend fun getCat(id: String): Result<Cat> {
        val cat = localSource.getCat(id)
        return if (cat != null) {
            Result.success(cat)
        } else {
            Result.failure(NoSuchElementException("Cat not found with id: $id"))
        }
    }
}