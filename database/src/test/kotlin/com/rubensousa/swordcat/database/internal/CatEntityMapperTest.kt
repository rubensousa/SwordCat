package com.rubensousa.swordcat.database.internal

import com.google.common.truth.Truth.assertThat
import com.rubensousa.swordcat.domain.Cat
import org.junit.Test

class CatEntityMapperTest {

    private val mapper = CatEntityMapper()

    @Test
    fun `mapping from entity to domain works correctly`() {
        // given
        val entity = CatEntity(
            id = "1",
            breedName = "Abyssinian",
            origin = "Egypt",
            temperament = "Active",
            description = "Smart cat",
            imageId = "imageId",
            lifespanStart = 12,
            lifespanEnd = 15
        )

        // when
        val domain = mapper.mapFromEntity(entity)

        // then
        assertThat(domain.id).isEqualTo(entity.id)
        assertThat(domain.breedName).isEqualTo(entity.breedName)
        assertThat(domain.origin).isEqualTo(entity.origin)
        assertThat(domain.temperament).isEqualTo(entity.temperament)
        assertThat(domain.description).isEqualTo(entity.description)
        assertThat(domain.imageId).isEqualTo(entity.imageId)
        assertThat(domain.lifespan.first).isEqualTo(entity.lifespanStart)
        assertThat(domain.lifespan.last).isEqualTo(entity.lifespanEnd)
    }

    @Test
    fun `mapping from domain to entity works correctly`() {
        // given
        val domain = Cat(
            id = "1",
            breedName = "Abyssinian",
            origin = "Egypt",
            temperament = "Active",
            description = "Smart cat",
            imageId = "imageId",
            lifespan = 12..15
        )

        // when
        val entity = mapper.mapToEntity(domain)

        // then
        assertThat(entity.id).isEqualTo(domain.id)
        assertThat(entity.breedName).isEqualTo(domain.breedName)
        assertThat(entity.origin).isEqualTo(domain.origin)
        assertThat(entity.temperament).isEqualTo(domain.temperament)
        assertThat(entity.description).isEqualTo(domain.description)
        assertThat(entity.imageId).isEqualTo(domain.imageId)
        assertThat(entity.lifespanStart).isEqualTo(domain.lifespan.first)
        assertThat(entity.lifespanEnd).isEqualTo(domain.lifespan.last)
    }
}
