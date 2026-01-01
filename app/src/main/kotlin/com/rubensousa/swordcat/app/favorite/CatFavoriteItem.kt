package com.rubensousa.swordcat.app.favorite

import com.rubensousa.swordcat.app.image.ImageReference

data class CatFavoriteItem(
    val id: String,
    val breedName: String,
    val imageReference: ImageReference?,
)
