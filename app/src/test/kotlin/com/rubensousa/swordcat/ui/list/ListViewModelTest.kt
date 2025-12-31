package com.rubensousa.swordcat.ui.list

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.rubensousa.carioca.junit4.rules.MainDispatcherRule
import com.rubensousa.swordcat.fixtures.CatFixtures
import com.rubensousa.swordcat.fixtures.FakeCatFavoriteLocalSource
import com.rubensousa.swordcat.fixtures.FakeCatRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeCatRepository()
    private val favoriteSource = FakeCatFavoriteLocalSource()
    private val unconfinedDispatcher = UnconfinedTestDispatcher()
    private val standardDispatcher = StandardTestDispatcher()
    private val config = ListConfig(searchDebounceMs = 300L)
    private lateinit var viewModel: ListViewModel

    @Test
    fun `content state is shown after successful load`() = runTest {
        // given
        val cats = listOf(CatFixtures.create(id = "1", breedName = "Abyssinian"))
        repository.setLoadCatsSuccessResult(cats)

        // when
        viewModel = createViewModel()

        // then
        val state = viewModel.getUiState().value as ListScreenState.Content
        assertThat(state.items).hasSize(1)
        assertThat(state.items[0].breedName).isEqualTo("Abyssinian")
    }

    @Test
    fun `error state is shown after failed load`() = runTest {
        // given
        repository.setLoadCatsErrorResult(IllegalStateException("Whoops"))

        // when
        viewModel = createViewModel()

        // then
        assertThat(viewModel.getUiState().value)
            .isInstanceOf(ListScreenState.Error::class.java)
    }

    @Test
    fun `retry loading cats updates state to content`() = runTest {
        // given
        repository.setLoadCatsErrorResult(IllegalStateException("Whoops"))
        viewModel = createViewModel()
        val errorState = viewModel.getUiState().value as ListScreenState.Error

        // when
        val cats = listOf(CatFixtures.create(id = "1"))
        repository.setLoadCatsSuccessResult(cats)
        errorState.onRetryClick()

        // then
        assertThat(viewModel.getUiState().value)
            .isInstanceOf(ListScreenState.Content::class.java)
    }

    @Test
    fun `toggling favorite updates item state`() = runTest {
        // given
        val catId = "1"
        val cats = listOf(CatFixtures.create(id = catId))
        repository.setLoadCatsSuccessResult(cats)
        viewModel = createViewModel()
        val state = viewModel.getUiState().value as ListScreenState.Content
        val item = state.items[0]

        // when
        item.onFavoriteClick()

        // then
        assertThat(favoriteSource.isFavorite(catId)).isTrue()
    }

    @Test
    fun `item favorite state is initialized correctly to favorite state`() = runTest {
        // given
        val catId = "1"
        val cats = listOf(CatFixtures.create(id = catId))
        repository.setLoadCatsSuccessResult(cats)
        favoriteSource.toggleFavorite(catId)

        // when
        viewModel = createViewModel()
        val state = viewModel.getUiState().value as ListScreenState.Content
        val item = state.items[0]

        // then
        assertThat(item.favoriteState.value).isTrue()
    }

    @Test
    fun `search is debounced`() = runTest {
        // given
        val cats = listOf(CatFixtures.create(id = "1", breedName = "Abyssinian"))
        repository.setLoadCatsSuccessResult(cats)
        viewModel = createViewModel(dispatcher = standardDispatcher)
        standardDispatcher.scheduler.advanceUntilIdle()
        val content = viewModel.getUiState().value as ListScreenState.Content

        viewModel.getUiState().test {
            skipItems(1)

            // when
            content.onSearchTextChanged("A")
            content.onSearchTextChanged("Ab")
            content.onSearchTextChanged("Aby")
            standardDispatcher.scheduler.advanceTimeBy(config.searchDebounceMs)

            // then: only one emission
            awaitItem()
            expectNoEvents()
        }
    }

    @Test
    fun `empty search query restores full list`() = runTest {
        // given
        val cats = listOf(
            CatFixtures.create(id = "1", breedName = "Abyssinian"),
            CatFixtures.create(id = "2", breedName = "Bengal")
        )
        repository.setLoadCatsSuccessResult(cats)
        val viewModel = createViewModel(dispatcher = standardDispatcher)
        standardDispatcher.scheduler.advanceUntilIdle()
        val content = viewModel.getUiState().value as ListScreenState.Content
        content.onSearchTextChanged("Abyss")

        // when
        standardDispatcher.scheduler.advanceTimeBy(config.searchDebounceMs)
        content.onSearchTextChanged("")

        // then
        val finalState = viewModel.getUiState().value as ListScreenState.Content
        assertThat(finalState.items).hasSize(2)
    }

    private fun createViewModel(
        dispatcher: CoroutineDispatcher = unconfinedDispatcher
    ): ListViewModel {
        return ListViewModel(
            repository = repository,
            favoriteSource = favoriteSource,
            dispatcher = dispatcher,
            listConfig = config
        )
    }

}
