package com.rubensousa.swordcat.ui.detail

import com.google.common.truth.Truth.assertThat
import com.rubensousa.carioca.junit4.rules.MainDispatcherRule
import com.rubensousa.swordcat.fixtures.CatFixtures
import com.rubensousa.swordcat.fixtures.FakeCatFavoriteLocalSource
import com.rubensousa.swordcat.fixtures.FakeCatRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeCatRepository()
    private val favoriteSource = FakeCatFavoriteLocalSource()
    private val dispatcher = UnconfinedTestDispatcher()
    private val catId = "1"
    private lateinit var viewModel: DetailViewModel

    @Test
    fun `content state is shown after successful load`() = runTest {
        // given
        val cat = CatFixtures.create(id = catId, breedName = "Abyssinian")
        repository.setGetCatSuccessResult(cat)

        // when
        viewModel = createViewModel()

        // then
        val state = viewModel.getUiState().value as DetailScreenState.Content
        assertThat(state.breedName).isEqualTo("Abyssinian")
    }

    @Test
    fun `error state is shown after failed load`() = runTest {
        // given
        repository.setGetCatErrorResult(NoSuchElementException("Not found"))

        // when
        viewModel = createViewModel()

        // then
        assertThat(viewModel.getUiState().value).isInstanceOf(DetailScreenState.Error::class.java)
    }

    @Test
    fun `toggling favorite updates repository`() = runTest {
        // given
        val cat = CatFixtures.create(id = catId)
        repository.setGetCatSuccessResult(cat)
        viewModel = createViewModel()
        val content = viewModel.getUiState().value as DetailScreenState.Content

        // when
        content.onFavoriteClick()

        // then
        assertThat(favoriteSource.isFavorite(catId)).isTrue()
    }

    private fun createViewModel(): DetailViewModel {
        return DetailViewModel(
            catId = catId,
            repository = repository,
            favoriteSource = favoriteSource,
            dispatcher = dispatcher
        )
    }
}
