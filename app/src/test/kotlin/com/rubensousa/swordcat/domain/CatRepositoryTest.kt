package com.rubensousa.swordcat.domain

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
        val localCats = emptyList<Cat>()
        val remoteCats = List(3) { CatFixtures.create() }
        localSource.saveCats(localCats)
        remoteSource.setLoadCatSuccessResult(remoteCats)

        // when
        val cats = repository.loadCats(CatRequest(limit = remoteCats.size, offset = 0))
            .getOrThrow()

        // then
        assertThat(cats).isEqualTo(remoteCats)
    }

    @Test
    fun `network error is returned to consumer`() = runTest {
        // given
        val errorCause = IllegalStateException("Whoops")
        remoteSource.setLoadCatErrorResult(errorCause)

        // when
        val error = repository.loadCats(CatRequestFixtures.create()).exceptionOrNull()

        // then
        assertThat(error).isEqualTo(errorCause)
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

}
