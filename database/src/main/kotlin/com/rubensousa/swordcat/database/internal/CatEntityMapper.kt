package com.rubensousa.swordcat.database.internal

import com.rubensousa.swordcat.domain.Cat

internal class CatEntityMapper {

    fun mapFromEntity(entity: CatEntity): Cat {
        return Cat(
            id = entity.id,
            breedName = entity.breedName,
            origin = entity.origin,
            temperament = entity.temperament,
            description = entity.description,
            imageId = entity.imageId,
            lifespan = entity.lifespanStart..entity.lifespanEnd
        )
    }

    fun mapToEntity(cat: Cat): CatEntity {
        return CatEntity(
            id = cat.id,
            breedName = cat.breedName,
            origin = cat.origin,
            temperament = cat.temperament,
            description = cat.description,
            imageId = cat.imageId,
            lifespanStart = cat.lifespan.first,
            lifespanEnd = cat.lifespan.last,
        )
    }

}