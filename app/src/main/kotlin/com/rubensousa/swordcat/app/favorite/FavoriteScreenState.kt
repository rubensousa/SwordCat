package com.rubensousa.swordcat.app.favorite

import kotlinx.collections.immutable.ImmutableList

data class FavoriteScreenState(
    val items: ImmutableList<CatFavoriteItem>,
    val averageLifespan: String
)
