package com.rubensousa.swordcat.app.navigation

import androidx.navigation3.runtime.NavKey
import com.rubensousa.swordcat.R
import com.rubensousa.swordcat.app.favorite.FavoriteNavKey
import com.rubensousa.swordcat.app.list.ListNavKey

enum class TopLevelDestination(
    val navKey: NavKey,
    val labelResource: Int,
    val iconResource: Int,
) {
    HOME(
        navKey = ListNavKey,
        labelResource = R.string.navigation_home,
        iconResource = R.drawable.ic_navigation_home,
    ),
    FAVORITES(
        navKey = FavoriteNavKey,
        labelResource = R.string.navigation_favorites,
        iconResource = R.drawable.ic_navigation_favorite
    ),
}