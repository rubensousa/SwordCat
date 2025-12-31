package com.rubensousa.swordcat.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells.Adaptive
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rubensousa.swordcat.ui.ConsumeEvents
import com.rubensousa.swordcat.ui.EventSource
import com.rubensousa.swordcat.ui.EventSourceFlow
import com.rubensousa.swordcat.ui.detail.DetailNavKey
import com.rubensousa.swordcat.ui.image.ImageReference
import com.rubensousa.swordcat.ui.navigation.LocalNavigator
import com.rubensousa.swordcat.ui.theme.SwordCatTheme
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun ListScreen() {
    val viewModel = hiltViewModel<ListViewModel>()
    ListScreen(
        state = viewModel.getUiState().collectAsStateWithLifecycle().value,
        events = viewModel.getEvents()
    )
}

@Composable
fun ListScreen(
    state: ListScreenState,
    events: EventSourceFlow<ListScreenEvent>
) {
    val navigator = LocalNavigator.current
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        topBar = {

        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
            ,
        ) {
            when (state) {
                is ListScreenState.Content -> {
                    LazyVerticalGrid(
                        modifier = Modifier
                            .weight(1f),
                        columns = Adaptive(minSize = 150.dp),
                        contentPadding = PaddingValues(vertical = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(state.items) { item ->
                            CatCell(
                                name = item.breedName,
                                imageReference = item.imageReference,
                                isFavorite = item.favoriteState.collectAsStateWithLifecycle().value,
                                onClick = item.onClick,
                                onFavoriteClick = item.onFavoriteClick
                            )
                        }
                    }
                }

                is ListScreenState.Error -> {

                }

                ListScreenState.Loading -> {

                }
            }
        }
    }
    ConsumeEvents(events) { event ->
        when (event) {
            is ListScreenEvent.OpenDetail -> {
                navigator.navigateTo(DetailNavKey(event.id))
            }
        }
    }
}

@Preview
@Composable
private fun PreviewListLoading() {
    SwordCatTheme {
        ListScreen(
            state = ListScreenState.Loading,
            events = EventSource.emptyFlow()
        )
    }
}


@Preview
@Composable
private fun PreviewListContent() {
    SwordCatTheme {
        ListScreen(
            state = ListScreenState.Content(
                items = List(10) { index ->
                    CatListItem(
                        breedName = index.toString(),
                        imageReference = ImageReference(index.toString()),
                        favoriteState = MutableStateFlow(index % 2 == 0),
                        onFavoriteClick = {},
                        onClick = {}
                    )
                }.toPersistentList(),
                onSearchTextChanged = {}
            ),
            events = EventSource.emptyFlow()
        )
    }
}