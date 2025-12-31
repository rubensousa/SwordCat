package com.rubensousa.swordcat.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import com.rubensousa.swordcat.R
import com.rubensousa.swordcat.domain.Cat
import com.rubensousa.swordcat.fixtures.FakeCatRemoteSource
import com.rubensousa.swordcat.ui.list.ListConfig
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class ListScreenController @AssistedInject constructor(
    @Assisted private val composeTestRule: ComposeTestRule,
    private val remoteCatRemoteSource: FakeCatRemoteSource,
    private val config: ListConfig
) {

    fun simulateCats(cats: List<Cat>) {
        remoteCatRemoteSource.setLoadCatSuccessResult(cats)
    }

    fun simulateCatFetchError() {
        remoteCatRemoteSource.setLoadCatErrorResult(IllegalStateException("Whoops"))
    }

    fun waitUntilLoadingIsGone() {
        composeTestRule.waitUntil {
            composeTestRule.onNodeWithContentDescription(getString(R.string.loading))
                .isNotDisplayed()
        }
    }

    fun assertErrorStateIsDisplayed() {
        composeTestRule.onNodeWithText(getString(R.string.error_generic))
            .assertIsDisplayed()
        composeTestRule.onNodeWithText(getString(R.string.retry))
            .assertIsDisplayed()
    }

    fun assertCatIsDisplayed(name: String) {
        composeTestRule.onNodeWithContentDescription(name)
            .assertIsDisplayed()
    }

    fun assertCatIsNotDisplayed(name: String) {
        composeTestRule.onNodeWithContentDescription(name)
            .assertIsNotDisplayed()
    }

    fun assertCatIsFavorite(name: String) {
        composeTestRule.onNode(
            hasAnyAncestor(hasContentDescription(name)) and
                    hasContentDescription(getString(R.string.favorite_on))
        ).assertIsDisplayed()
    }

    fun assertCatIsNotFavorite(name: String) {
        composeTestRule.onNode(
            hasAnyAncestor(hasContentDescription(name)) and
                    hasContentDescription(getString(R.string.favorite_off))
        ).assertIsDisplayed()
    }

    fun performRetry() {
        composeTestRule.onNodeWithText(getString(R.string.retry))
            .performClick()
    }

    fun performCatClick(name: String) {
        composeTestRule.onNodeWithContentDescription(name)
            .performClick()
    }

    fun performFavoriteClick(name: String) {
        composeTestRule.onNode(
            hasAnyAncestor(hasContentDescription(name)) and
                    (hasContentDescription(getString(R.string.favorite_on)) or
                            hasContentDescription(getString(R.string.favorite_off)))
        ).performClick()
    }

    fun performSearch(text: String) {
        composeTestRule.onNodeWithContentDescription(getString(R.string.text_field_search))
            .performTextInput(text)
        // Wait for search debounce
        Thread.sleep(config.searchDebounceMs)
    }

    private fun getString(id: Int): String {
        return InstrumentationRegistry.getInstrumentation().targetContext.getString(id)
    }

    @AssistedFactory
    interface Factory {
        fun create(composeTestRule: ComposeTestRule): ListScreenController
    }

}
