package com.rubensousa.swordcat.backend

import com.rubensousa.swordcat.backend.internal.CatService
import com.rubensousa.swordcat.domain.Cat
import com.rubensousa.swordcat.domain.CatRemoteSource
import com.rubensousa.swordcat.domain.CatRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CatHttpSource @Inject internal constructor(
    private val service: CatService,
    private val dispatcher: CoroutineDispatcher,
) : CatRemoteSource {

    override suspend fun loadCats(request: CatRequest): Result<List<Cat>> {
        return withContext(dispatcher) {
            runCatching {
                val remoteCats = service.getBreeds(
                    limit = request.limit,
                    page = request.offset / request.limit
                )
                remoteCats.mapNotNull { remoteCat ->
                    val lifespan = parseLifespan(remoteCat.lifeSpan)
                    if (lifespan != null) {
                        Cat(
                            id = remoteCat.id,
                            breedName = remoteCat.breedName,
                            origin = remoteCat.origin,
                            temperament = remoteCat.temperament,
                            description = remoteCat.description,
                            imageId = remoteCat.imageId,
                            lifespan = lifespan
                        )
                    } else {
                        null
                    }
                }
            }
        }
    }

    private fun parseLifespan(value: String): IntRange? {
        return runCatching {
            val values = value.replace(" ", "").split("-")
            values[0].toInt()..values[1].toInt()
        }.getOrNull()
    }
}