package com.rubensousa.swordcat.fixtures

import com.rubensousa.swordcat.domain.Cat

object CatFixtures {

    fun create(
        id: String = "",
        breedName: String = "",
        origin: String = "",
        temperament: String = "",
        description: String = "",
        imageId: String = "",
        lifespan: IntRange = 1..20
    ): Cat {
        return Cat(
            id = id,
            breedName = breedName,
            origin = origin,
            temperament = temperament,
            description = description,
            imageId = imageId,
            lifespan = lifespan
        )
    }

}