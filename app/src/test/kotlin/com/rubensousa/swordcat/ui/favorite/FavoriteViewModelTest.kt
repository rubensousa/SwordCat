package com.rubensousa.swordcat.ui.favorite

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.rubensousa.carioca.junit4.rules.MainDispatcherRule
import com.rubensousa.swordcat.domain.fixtures.CatFixtures
import com.rubensousa.swordcat.domain.fixtures.FakeCatFavoriteLocalSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavoriteViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val favoriteSource = FakeCatFavoriteLocalSource()
    private lateinit var viewModel: FavoriteViewModel

    @Test
    fun `content is empty by default`() = runTest {
        // given
        viewModel = createViewModel()

        // then
        assertThat(viewModel.uiState.value.items).isEmpty()
        assertThat(viewModel.uiState.value.averageLifespan).isEqualTo("0.0")
    }

    @Test
    fun `content is updated when a cat is marked as favorite`() = runTest {
        // given
        val cat = CatFixtures.create(id = "1", breedName = "Abyssinian", lifespan = 12..13)
        favoriteSource.setAvailableCats(listOf(cat))
        viewModel = createViewModel()

        viewModel.uiState.test {
            skipItems(1)

            // when
            favoriteSource.toggleFavorite(cat.id)

            // then
            val state = awaitItem()
            assertThat(state.items).hasSize(1)
        }
    }

    @Test
    fun `average lifespan is calculated correctly for multiple cats`() = runTest {
        // given
        val cat1 = CatFixtures.create(id = "1", lifespan = 10..12)
        val cat2 = CatFixtures.create(id = "2", lifespan = 14..16)
        favoriteSource.setAvailableCats(listOf(cat1, cat2))
        viewModel = createViewModel()

        viewModel.uiState.test {
            skipItems(1)

            // when
            favoriteSource.toggleFavorite(cat1.id)
            skipItems(1)
            favoriteSource.toggleFavorite(cat2.id)

            // then
            // (10 + 14) / 2 = 12.0
            assertThat(awaitItem().averageLifespan).isEqualTo("12.0")
        }
    }

    private fun createViewModel(): FavoriteViewModel {
        return FavoriteViewModel(favoriteSource = favoriteSource)
    }

}
