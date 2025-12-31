package com.rubensousa.swordcat.ui.cat

import com.rubensousa.swordcat.ui.image.ImageReference
import kotlinx.coroutines.flow.StateFlow

data class CatListItem(
    val breedName: String,
    val imageReference: ImageReference?,
    val favoriteState: StateFlow<Boolean>,
    val onFavoriteClick: () -> Unit,
    val onClick: () -> Unit
)