package com.rubensousa.swordcat.ui.list

import com.google.common.truth.Truth
import com.rubensousa.carioca.junit4.rules.MainDispatcherRule
import com.rubensousa.swordcat.fixtures.CatFixtures
import com.rubensousa.swordcat.fixtures.FakeCatRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeCatRepository()
    private val testDispatcher = UnconfinedTestDispatcher()
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
        Truth.assertThat(state.items).hasSize(1)
        Truth.assertThat(state.items[0].breedName).isEqualTo("Abyssinian")
    }

    @Test
    fun `error state is shown after failed load`() = runTest {
        // given
        repository.setLoadCatsErrorResult(IllegalStateException("Whoops"))

        // when
        viewModel = createViewModel()

        // then
        Truth.assertThat(viewModel.getUiState().value)
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
        Truth.assertThat(viewModel.getUiState().value)
            .isInstanceOf(ListScreenState.Content::class.java)
    }

    private fun createViewModel() = ListViewModel(
        repository = repository,
        dispatcher = testDispatcher
    )
}