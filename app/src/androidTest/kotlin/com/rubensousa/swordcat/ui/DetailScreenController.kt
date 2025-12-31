package com.rubensousa.swordcat.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.rubensousa.swordcat.R
import com.rubensousa.swordcat.domain.Cat
import com.rubensousa.swordcat.fixtures.FakeCatRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class DetailScreenController @AssistedInject constructor(
    @Assisted private val composeTestRule: ComposeTestRule,
    private val repository: FakeCatRepository,
) {

    fun simulateCat(cat: Cat) {
        repository.setGetCatSuccessResult(cat)
    }

    fun waitUntilLoadingIsGone() {
        composeTestRule.waitUntil {
            composeTestRule.onNodeWithContentDescription(getString(R.string.loading))
                .isNotDisplayed()
        }
    }

    fun assertInfoItemIsDisplayed(label: Int, value: String) {
        composeTestRule.onNodeWithText(getString(label)).assertIsDisplayed()
        composeTestRule.onNode(
            hasText(value)
                .and(hasAnyAncestor(hasContentDescription(getString(label))))
        ).assertIsDisplayed()
    }

    @AssistedFactory
    interface Factory {
        fun create(composeTestRule: ComposeTestRule): DetailScreenController
    }
}
