package com.rubensousa.swordcat.ui.list

import com.rubensousa.swordcat.domain.Cat
import com.rubensousa.swordcat.ui.image.ImageReference
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class CatListItemMapper @Inject constructor(

) {

    fun map(
        cat: Cat,
        onAction: (CatListItemAction) -> Unit
    ): CatListItem {
        return CatListItem(
            breedName = cat.breedName,
            imageReference = cat.imageId?.let { ImageReference(it) },
            favoriteState = MutableStateFlow(false),
            onFavoriteClick = {
                onAction(CatListItemAction.FavoriteClicked(cat))
            },
            onClick = {
                onAction(CatListItemAction.Clicked(cat))
            }
        )
    }

}