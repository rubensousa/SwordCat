package com.rubensousa.swordcat.app

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.rubensousa.swordcat.app.detail.DetailNavKey
import com.rubensousa.swordcat.app.detail.DetailScreen
import com.rubensousa.swordcat.app.favorite.FavoriteNavKey
import com.rubensousa.swordcat.app.favorite.FavoriteScreen
import com.rubensousa.swordcat.app.list.ListNavKey
import com.rubensousa.swordcat.app.list.ListScreen
import com.rubensousa.swordcat.app.navigation.AppNavigatorImpl
import com.rubensousa.swordcat.app.navigation.LocalNavigator
import com.rubensousa.swordcat.app.navigation.TopLevelDestination
import com.rubensousa.swordcat.app.navigation.rememberNavigationState
import com.rubensousa.swordcat.app.navigation.toEntries
import com.rubensousa.swordcat.app.theme.SwordCatTheme

@Composable
fun MainScreen() {
    val navigationState =
        rememberNavigationState(
            startRoute = TopLevelDestination.HOME.navKey,
            topLevelRoutes = TopLevelDestination.entries.map { entry -> entry.navKey }
                .toSet()
        )
    val navigator = remember {
        AppNavigatorImpl(
            navigationState
        )
    }

    val entryProvider = entryProvider {
        entry<ListNavKey> { ListScreen() }
        entry<FavoriteNavKey> { FavoriteScreen() }
        entry<DetailNavKey> { entry ->
            DetailScreen(
                entry.id
            )
        }
    }

    CompositionLocalProvider(LocalNavigator provides navigator) {
        NavigationSuiteScaffold(
            navigationSuiteItems = {
                TopLevelDestination.entries.forEach { topLevelDestination ->
                    item(
                        icon = {
                            Icon(
                                painter = painterResource(topLevelDestination.iconResource),
                                contentDescription = stringResource(topLevelDestination.labelResource)
                            )
                        },
                        label = {
                            Text(
                                text = stringResource(topLevelDestination.labelResource)
                            )
                        },
                        selected = topLevelDestination.navKey == navigationState.topLevelRoute,
                        onClick = {
                            navigationState.topLevelRoute = topLevelDestination.navKey
                        }
                    )
                }
            }
        ) {
            NavDisplay(
                entries = navigationState.toEntries(entryProvider),
                onBack = { navigator.navigateBack() },
                transitionSpec = {
                    // Slide in from right when navigating forward
                    slideInHorizontally(initialOffsetX = { it }) togetherWith
                            slideOutHorizontally(targetOffsetX = { -it })
                },
                popTransitionSpec = {
                    // Slide in from left when navigating back
                    slideInHorizontally(initialOffsetX = { -it }) togetherWith
                            slideOutHorizontally(targetOffsetX = { it })
                },
                predictivePopTransitionSpec = {
                    // Slide in from left when navigating back
                    slideInHorizontally(initialOffsetX = { -it }) togetherWith
                            slideOutHorizontally(targetOffsetX = { it })
                },
            )
        }
    }
}

@PreviewScreenSizes
@Composable
private fun MainScreenPreview() {
    SwordCatTheme {
        MainScreen()
    }
}