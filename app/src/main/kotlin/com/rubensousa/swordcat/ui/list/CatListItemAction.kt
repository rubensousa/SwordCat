package com.rubensousa.swordcat.ui.list

import com.rubensousa.swordcat.domain.Cat

sealed interface CatListItemAction {
    data class Clicked(val cat: Cat): CatListItemAction
    data class FavoriteClicked(val cat: Cat): CatListItemAction
}
