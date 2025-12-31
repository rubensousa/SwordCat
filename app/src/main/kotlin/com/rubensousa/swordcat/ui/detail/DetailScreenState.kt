package com.rubensousa.swordcat.ui.detail

import com.rubensousa.swordcat.ui.StringResource
import com.rubensousa.swordcat.ui.image.ImageReference
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.StateFlow

sealed interface DetailScreenState {
    data object Loading : DetailScreenState
    data class Error(val message: StringResource) : DetailScreenState
    data class Content(
        val breedName: String,
        val infoItems: ImmutableList<DetailInfoItem>,
        val imageReference: ImageReference?,
        val isFavorite: StateFlow<Boolean>,
        val onFavoriteClick: () -> Unit,
    ) : DetailScreenState
}
