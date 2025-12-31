package com.rubensousa.swordcat.ui.favorite

import com.rubensousa.swordcat.ui.image.ImageReference

data class CatFavoriteItem(
    val breedName: String,
    val imageReference: ImageReference?,
    val onClick: () -> Unit
)
