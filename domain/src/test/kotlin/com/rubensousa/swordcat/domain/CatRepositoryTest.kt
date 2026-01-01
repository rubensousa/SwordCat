package com.rubensousa.swordcat.domain

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.rubensousa.swordcat.domain.fixtures.CatFixtures
import com.rubensousa.swordcat.domain.fixtures.CatRequestFixtures
import com.rubensousa.swordcat.domain.fixtures.FakeCatLocalSource
import com.rubensousa.swordcat.domain.fixtures.FakeCatRemoteSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test

class CatRepositoryTest {

    private val localSource = FakeCatLocalSource()
    private val remoteSource = FakeCatRemoteSource()
    private val defaultRequest = CatRequestFixtures.create()
    private val repository = CatRepositoryImpl(
        localSource = localSource,
        remoteSource = remoteSource
    )

    @Test
    fun `cats load from local source by default`() = runTest {
        // given
        val localCats = List(3) { CatFixtures.create(id = it.toString()) }
        localSource.saveCats(localCats)

        // when
        val cats = repository.loadCats(CatRequest(limit = localCats.size, offset = 0))
            .first().getOrThrow()

        // then
        assertThat(cats).isEqualTo(localCats)
    }

    @Test
    fun `remote content is returned if local source does not have content`() = runTest {
        // given
        val remoteCats = List(3) { CatFixtures.create(id = it.toString()) }
        remoteSource.setLoadCatSuccessResult(remoteCats)

        // when
        val cats = repository.loadCats(defaultRequest).first().getOrThrow()

        // then
        assertThat(cats).isEqualTo(remoteCats)
    }

    @Test
    fun `remote content is saved to local source`() = runTest {
        // given
        val remoteCats = List(3) { CatFixtures.create(id = it.toString()) }
        remoteSource.setLoadCatSuccessResult(remoteCats)
        repository.loadCats(defaultRequest).first().getOrThrow()

        // when
        val cats = localSource.loadCats(CatRequestFixtures.create())

        // then
        assertThat(cats).isEqualTo(remoteCats)
    }

    @Test
    fun `remote content update is not returned if it matches the local content`() = runTest {
        // given
        val remoteCats = List(3) { CatFixtures.create(id = it.toString()) }
        val localCats = remoteCats.toList()
        remoteSource.setLoadCatSuccessResult(remoteCats)
        localSource.saveCats(localCats)

        repository.loadCats(defaultRequest).test {
            // when
            skipItems(1)

            // then
            awaitComplete()
        }
    }

    @Test
    fun `remote content update is returned if it is different than local`() = runTest {
        // given
        val remoteCats = List(10) { CatFixtures.create(id = it.toString()) }
        val localCats = remoteCats.subList(0, 3)
        remoteSource.setLoadCatSuccessResult(remoteCats)
        localSource.saveCats(localCats)

        repository.loadCats(defaultRequest).test {
            // when
            skipItems(1)

            // then
            assertThat(awaitItem().getOrThrow()).isEqualTo(remoteCats)
            awaitComplete()
        }
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

}
