package com.rubensousa.swordcat.domain

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.rubensousa.swordcat.domain.internal.CatRepositoryImpl
import com.rubensousa.swordcat.fixtures.CatFixtures
import com.rubensousa.swordcat.fixtures.CatRequestFixtures
import com.rubensousa.swordcat.fixtures.FakeCatLocalSource
import com.rubensousa.swordcat.fixtures.FakeCatRemoteSource
import kotlinx.coroutines.test.runTest
import org.junit.Test

class CatRepositoryTest {

    private val localSource = FakeCatLocalSource()
    private val remoteSource = FakeCatRemoteSource()
    private val repository = CatRepositoryImpl(
        localSource = localSource,
        remoteSource = remoteSource
    )

    @Test
    fun `cats load from remote source by default`() = runTest {
        // given
        val remoteCats = List(3) { CatFixtures.create() }
        remoteSource.setLoadCatSuccessResult(remoteCats)

        // when
        val cats = repository.loadCats(CatRequest(limit = remoteCats.size, offset = 0))
            .getOrThrow()

        // then
        assertThat(cats).isEqualTo(remoteCats)
    }

    @Test
    fun `local content is returned if remote source fails`() = runTest {
        // given
        val errorCause = IllegalStateException("Whoops")
        val localCats = List(3) { CatFixtures.create(id = it.toString()) }
        localSource.saveCats(localCats)
        remoteSource.setLoadCatErrorResult(errorCause)

        // when
        val cats = repository.loadCats(CatRequestFixtures.create()).getOrThrow()

        // then
        assertThat(cats).isEqualTo(localCats)
    }

    @Test
    fun `getCat returns cat from local source`() = runTest {
        // given
        val cat = CatFixtures.create(id = "1")
        localSource.saveCats(listOf(cat))

        // when
        val result = repository.getCat(cat.id).getOrThrow()

        // then
        assertThat(result).isEqualTo(cat)
    }

    @Test
    fun `getCat returns error if cat is not found`() = runTest {
        // when
        val result = repository.getCat("1")

        // then
        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `toggleFavorite updates favorite state`() = runTest {
        // given
        val catId = "1"

        // when
        repository.toggleFavorite(catId)

        // then
        assertThat(repository.isFavorite(catId)).isTrue()
    }

    @Test
    fun `observeFavoriteState emits updates`() = runTest {
        // given
        val catId = "1"


        repository.observeFavoriteState(catId).test {
            assertThat(awaitItem()).isFalse()

            // when
            repository.toggleFavorite(catId)

            // then
            assertThat(awaitItem()).isTrue()
        }
    }

}
