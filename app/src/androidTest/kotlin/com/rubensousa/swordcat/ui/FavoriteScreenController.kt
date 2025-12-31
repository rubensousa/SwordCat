package com.rubensousa.swordcat.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.rubensousa.swordcat.R
import com.rubensousa.swordcat.domain.Cat
import com.rubensousa.swordcat.domain.CatFavoriteLocalSource
import com.rubensousa.swordcat.domain.CatLocalSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.runBlocking

class FavoriteScreenController @AssistedInject constructor(
    @Assisted private val composeTestRule: ComposeTestRule,
    private val catLocalSource: CatLocalSource,
    private val catFavoriteLocalSource: CatFavoriteLocalSource,
) {

    fun simulateFavoriteCats(cats: List<Cat>) {
        runBlocking {
            catLocalSource.saveCats(cats)
            cats.forEach { cat ->
                if (!catFavoriteLocalSource.isFavorite(cat.id)) {
                    catFavoriteLocalSource.toggleFavorite(cat.id)
                }
            }
        }
    }

    fun assertCatIsDisplayed(name: String) {
        composeTestRule.onNodeWithContentDescription(name)
            .assertIsDisplayed()
    }

    fun assertAverageLifespan(average: String) {
        composeTestRule.onNodeWithText(getString(R.string.favorite_lifespan, average))
            .assertIsDisplayed()
    }

    fun assertEmptyStateIsDisplayed() {
        assertAverageLifespan("0.0")
    }

    @AssistedFactory
    interface Factory {
        fun create(composeTestRule: ComposeTestRule): FavoriteScreenController
    }
}
