package com.rubensousa.swordcat.ui.favorite

import kotlinx.collections.immutable.ImmutableList

data class FavoriteScreenState(
    val items: ImmutableList<CatFavoriteItem>,
    val averageLifespan: String
)
