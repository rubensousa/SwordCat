package com.rubensousa.swordcat.database

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.rubensousa.swordcat.database.internal.CatEntityMapper
import com.rubensousa.swordcat.fixtures.CatFixtures
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class CatFavoriteDatabaseSourceTest {

    private val database = Room.inMemoryDatabaseBuilder(
        InstrumentationRegistry.getInstrumentation().targetContext,
        CatDatabase::class.java
    ).build()

    private val catDatabaseSource = CatDatabaseSource(
        database = database,
        entityMapper = CatEntityMapper(),
        dispatcher = UnconfinedTestDispatcher()
    )

    private val favoriteDatabaseSource = CatFavoriteDatabaseSource(
        database = database,
        entityMapper = CatEntityMapper(),
        dispatcher = UnconfinedTestDispatcher()
    )

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testFavoriteIsAdded() = runTest {
        // given
        val catId = "some_id"
        catDatabaseSource.saveCats(listOf(CatFixtures.create(id = catId)))

        // when
        favoriteDatabaseSource.toggleFavorite(catId)

        // then
        assertThat(favoriteDatabaseSource.observeIsFavorite(catId).first()).isTrue()
    }

    @Test
    fun testFavoriteIsRemoved() = runTest {
        // given
        val catId = "some_id"
        catDatabaseSource.saveCats(listOf(CatFixtures.create(id = catId)))
        favoriteDatabaseSource.toggleFavorite(catId)

        // when
        favoriteDatabaseSource.toggleFavorite(catId)

        // then
        assertThat(favoriteDatabaseSource.observeIsFavorite(catId).first()).isFalse()
    }

    @Test
    fun testObserveFavoriteCatsOnlyReturnsFavorites() = runTest {
        // given
        val cat1 = CatFixtures.create(id = "1", breedName = "A")
        val cat2 = CatFixtures.create(id = "2", breedName = "B")
        catDatabaseSource.saveCats(listOf(cat1, cat2))
        favoriteDatabaseSource.toggleFavorite(cat1.id)

        // when
        val favorites = favoriteDatabaseSource.observeFavoriteCats().first()

        // then
        assertThat(favorites).isEqualTo(listOf(cat1))
    }

    @Test
    fun testObserveFavoriteCatsAreSortedByName() = runTest {
        // given
        val cat1 = CatFixtures.create(id = "1", breedName = "Z")
        val cat2 = CatFixtures.create(id = "2", breedName = "A")
        catDatabaseSource.saveCats(listOf(cat1, cat2))
        favoriteDatabaseSource.toggleFavorite(cat1.id)
        favoriteDatabaseSource.toggleFavorite(cat2.id)

        // when
        val favorites = favoriteDatabaseSource.observeFavoriteCats().first()

        // then
        assertThat(favorites).isEqualTo(listOf(cat2, cat1))
    }
}
