package com.rubensousa.swordcat.ui

import com.rubensousa.carioca.hilt.compose.createHiltComposeRule
import com.rubensousa.swordcat.R
import com.rubensousa.swordcat.app.detail.DetailScreen
import com.rubensousa.swordcat.domain.fixtures.CatFixtures
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class DetailScreenTest {

    @get:Rule
    val composeTestRule = createHiltComposeRule()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var screenControllerFactory: DetailScreenController.Factory

    private val screenController by lazy {
        screenControllerFactory.create(composeTestRule)
    }

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun testCatDetailsAreDisplayed() {
        // given
        val cat = CatFixtures.create(
            id = "1",
            breedName = "Abyssinian",
            description = "Smart cat",
            temperament = "Gentle",
            origin = "Nowhere"
        )
        screenController.simulateCat(cat)

        // when
        launchScreen(cat.id)
        screenController.waitUntilLoadingIsGone()

        // then
        screenController.assertInfoItemIsDisplayed(
            R.string.label_breed,
            cat.breedName
        )
        screenController.assertInfoItemIsDisplayed(
            R.string.label_description,
            cat.description
        )
        screenController.assertInfoItemIsDisplayed(
            R.string.label_origin,
            cat.origin
        )
        screenController.assertInfoItemIsDisplayed(
            R.string.label_temperament,
            cat.temperament
        )
    }

    private fun launchScreen(id: String) {
        composeTestRule.setContent {
            DetailScreen(id = id)
        }
    }
}
