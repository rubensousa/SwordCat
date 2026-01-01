package com.rubensousa.swordcat.app.list

import com.rubensousa.swordcat.app.ui.StringResource
import kotlinx.collections.immutable.ImmutableList

sealed interface ListScreenState {
    data object Loading : ListScreenState
    data class Error(
        val message: StringResource,
        val onRetryClick: () -> Unit,
    ) : ListScreenState
    data class Content(
        val items: ImmutableList<CatListItem>,
        val onSearchTextChanged: (String) -> Unit,
    ): ListScreenState
}