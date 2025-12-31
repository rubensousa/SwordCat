package com.rubensousa.swordcat.ui

import com.rubensousa.carioca.hilt.compose.createHiltComposeRule
import com.rubensousa.swordcat.fixtures.CatFixtures
import com.rubensousa.swordcat.ui.list.ListScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class ListScreenTest {

    @get:Rule
    val composeTestRule = createHiltComposeRule()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var screenControllerFactory: ListScreenController.Factory

    private val screenController by lazy {
        screenControllerFactory.create(composeTestRule)
    }
    private val defaultCats = List(10) { id ->
        CatFixtures.create(
            id = id.toString(),
            breedName = "Breed: $id"
        )
    }

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun testCatsAreDisplayed() {
        // given
        screenController.simulateCats(defaultCats)
        launchScreen()

        // when
        screenController.waitUntilLoadingIsGone()

        // then
        screenController.assertCatIsDisplayed(defaultCats.first().breedName)
    }

    @Test
    fun testSearchFiltersCats() {
        // given
        val firstCat = defaultCats.first()
        val secondCat = defaultCats[1]
        screenController.simulateCats(defaultCats)
        launchScreen()

        // when
        screenController.performSearch(firstCat.breedName)

        // then
        screenController.assertCatIsDisplayed(firstCat.breedName)
        screenController.assertCatIsNotDisplayed(secondCat.breedName)
    }

    @Test
    fun testErrorStateIsDisplayed() {
        // given
        screenController.simulateCatFetchError()

        // when
        launchScreen()

        // then
        screenController.assertErrorStateIsDisplayed()
    }

    @Test
    fun testContentIsDisplayedAfterErrorRetry() {
        // given
        screenController.simulateCatFetchError()
        launchScreen()

        // when
        screenController.simulateCats(defaultCats)
        screenController.performRetry()
        screenController.waitUntilLoadingIsGone()

        // then
        screenController.assertCatIsDisplayed(defaultCats.first().breedName)
    }

    @Test
    fun testFavoriteTogglingOn() {
        // given
        val cat = defaultCats.first()
        screenController.simulateCats(listOf(cat))
        launchScreen()
        screenController.waitUntilLoadingIsGone()

        // when
        screenController.performFavoriteClick(cat.breedName)
        screenController.waitUntilCatIsFavorite(cat.breedName)

        // then
        screenController.assertCatIsFavorite(cat.breedName)
    }

    @Test
    fun testFavoriteTogglingOff() {
        // given
        val cat = defaultCats.first()
        screenController.simulateCats(listOf(cat))
        launchScreen()
        screenController.waitUntilLoadingIsGone()
        screenController.performFavoriteClick(cat.breedName)
        screenController.waitUntilCatIsFavorite(cat.breedName)

        // when
        screenController.performFavoriteClick(cat.breedName)

        // then
        screenController.assertCatIsNotFavorite(cat.breedName)
    }

    private fun launchScreen() {
        composeTestRule.setContent {
            ListScreen()
        }
    }

}
