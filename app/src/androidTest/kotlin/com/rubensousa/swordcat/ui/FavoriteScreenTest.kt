package com.rubensousa.swordcat.ui

import com.rubensousa.carioca.hilt.compose.createHiltComposeRule
import com.rubensousa.swordcat.fixtures.CatFixtures
import com.rubensousa.swordcat.ui.favorite.FavoriteScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class FavoriteScreenTest {

    @get:Rule
    val composeTestRule = createHiltComposeRule()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var screenControllerFactory: FavoriteScreenController.Factory

    private val screenController by lazy {
        screenControllerFactory.create(composeTestRule)
    }

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun testFavoriteCatsAreDisplayed() {
        // given
        val cats = listOf(
            CatFixtures.create(id = "1", breedName = "Abyssinian", lifespan = 12..15),
            CatFixtures.create(id = "2", breedName = "Bengal", lifespan = 10..16)
        )
        screenController.simulateFavoriteCats(cats)

        // when
        launchScreen()

        // then
        screenController.assertCatIsDisplayed("Abyssinian")
        screenController.assertCatIsDisplayed("Bengal")
    }

    @Test
    fun testAverageLifespanIsDisplayed() {
        // given
        val cats = listOf(
            CatFixtures.create(id = "1", lifespan = 10..10), // Average: 10.0
        )
        screenController.simulateFavoriteCats(cats)

        // when
        launchScreen()

        // then
        screenController.assertAverageLifespan("10.0")
    }

    @Test
    fun testEmptyState() {
        // given
        screenController.simulateFavoriteCats(emptyList())

        // when
        launchScreen()

        // then
        screenController.assertEmptyStateIsDisplayed()
    }

    private fun launchScreen() {
        composeTestRule.setContent {
            FavoriteScreen()
        }
    }
}
