package com.rubensousa.swordcat.ui.list

import com.rubensousa.swordcat.ui.StringResource
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