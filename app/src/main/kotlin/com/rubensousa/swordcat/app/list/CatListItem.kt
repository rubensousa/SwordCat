package com.rubensousa.swordcat.app.list

import com.rubensousa.swordcat.app.image.ImageReference
import kotlinx.coroutines.flow.StateFlow

data class CatListItem(
    val id: String,
    val breedName: String,
    val imageReference: ImageReference?,
    val favoriteState: StateFlow<Boolean>,
    val onFavoriteClick: () -> Unit,
)
