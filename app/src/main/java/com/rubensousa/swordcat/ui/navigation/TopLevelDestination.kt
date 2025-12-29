package com.rubensousa.swordcat.ui.navigation

import androidx.navigation3.runtime.NavKey
import com.rubensousa.swordcat.R
import com.rubensousa.swordcat.ui.favorite.FavoriteNavKey
import com.rubensousa.swordcat.ui.list.ListNavKey

enum class TopLevelDestination(
    val navKey: NavKey,
    val label: String,
    val iconResource: Int,
) {
    HOME(
        navKey = ListNavKey,
        label = "Home",
        iconResource = R.drawable.ic_navigation_home,
    ),
    FAVORITES(
        navKey = FavoriteNavKey,
        label = "Favorites",
        iconResource = R.drawable.ic_navigation_favorite
    ),
}