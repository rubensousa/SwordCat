package com.rubensousa.swordcat.database

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.rubensousa.swordcat.domain.CatRequest
import com.rubensousa.swordcat.fixtures.CatFixtures
import kotlinx.coroutines.test.runTest
import org.junit.Test

class CatDatabaseSourceTest {

    private val database = Room.inMemoryDatabaseBuilder(
        InstrumentationRegistry.getInstrumentation().targetContext,
        CatDatabase::class.java
    ).build()

    private val databaseSource = CatDatabaseSource(database)

    @Test
    fun testCatIsSaved() = runTest {
        // given
        val cat = CatFixtures.create(id = "some_id")

        // when
        databaseSource.saveCats(listOf(cat))

        // then
        assertThat(
            databaseSource.loadCats(
                CatRequest(
                    offset = 0,
                    limit = 1
                )
            )
        ).isEqualTo(listOf(cat))
    }

    @Test
    fun testCatsAreSortedByTheirNames() = runTest {
        // given
        val cats = listOf(
            CatFixtures.create(id = "id1", breedName = "z"),
            CatFixtures.create(id = "id2", breedName = "a"),
            CatFixtures.create(id = "id3", breedName = "b"),
        )

        // when
        databaseSource.saveCats(cats)

        // then
        assertThat(
            databaseSource.loadCats(
                CatRequest(
                    offset = 0,
                    limit = 3
                )
            )
        ).isEqualTo(cats.sortedBy { it.breedName })
    }
}
