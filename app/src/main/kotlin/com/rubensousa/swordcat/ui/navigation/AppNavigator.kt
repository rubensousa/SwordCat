package com.rubensousa.swordcat.ui.navigation

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation3.runtime.NavKey

interface AppNavigator {
    fun navigateTo(key: NavKey)
    fun navigateBack()
}

class AppNavigatorImpl(
    private val navigationState: NavigationState,
) : AppNavigator {

    override fun navigateTo(key: NavKey) {
        if (key in navigationState.backStacks.keys) {
            navigationState.topLevelRoute = key
        } else {
            navigationState.getCurrentBackStack().add(key)
        }
    }

    override fun navigateBack() {
        val backStack = navigationState.getCurrentBackStack()
        val currentRoute = backStack.last()
        // If we're at the base of the current route, go back to the start route stack.
        if (currentRoute == navigationState.topLevelRoute) {
            navigationState.topLevelRoute = navigationState.startRoute
        } else {
            backStack.removeLastOrNull()
        }
    }

}

val LocalNavigator: ProvidableCompositionLocal<AppNavigator> =
    compositionLocalOf {
        object : AppNavigator {
            override fun navigateTo(key: NavKey) {

            }

            override fun navigateBack() {
            }
        }
    }