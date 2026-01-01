package com.rubensousa.swordcat.app.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rubensousa.swordcat.R
import com.rubensousa.swordcat.app.detail.DetailNavKey
import com.rubensousa.swordcat.app.image.ImageReference
import com.rubensousa.swordcat.app.navigation.LocalNavigator
import com.rubensousa.swordcat.app.theme.SwordCatTheme
import com.rubensousa.swordcat.app.ui.CatGridConfig
import com.rubensousa.swordcat.app.ui.text
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun ListScreen() {
    val viewModel = hiltViewModel<ListViewModel>()
    ListScreen(
        state = viewModel.getUiState().collectAsStateWithLifecycle().value,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    state: ListScreenState,
) {
    val navigator = LocalNavigator.current
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
        ) {
            when (state) {
                is ListScreenState.Content -> {
                    var searchText by rememberSaveable { mutableStateOf("") }
                    val placeholderText = stringResource(R.string.text_field_search)
                    SearchBar(
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                            .fillMaxWidth(),
                        state = rememberSearchBarState(),
                        inputField = {
                            TextField(
                                modifier = Modifier.semantics {
                                    contentDescription = placeholderText
                                },
                                value = searchText,
                                maxLines = 1,
                                placeholder = {
                                    Text(placeholderText)
                                },
                                onValueChange = { text ->
                                    searchText = text
                                    state.onSearchTextChanged(text)
                                },
                            )
                        }
                    )
                    LazyVerticalGrid(
                        modifier = Modifier
                            .weight(1f),
                        columns = CatGridConfig.getCells(),
                        contentPadding = PaddingValues(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(state.items) { item ->
                            CatListCell(
                                name = item.breedName,
                                imageReference = item.imageReference,
                                isFavorite = item.favoriteState.collectAsStateWithLifecycle().value,
                                onClick = {
                                    navigator.navigateTo(DetailNavKey(item.id))
                                },
                                onFavoriteClick = item.onFavoriteClick
                            )
                        }
                    }
                }

                is ListScreenState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = state.message.text())
                            Button(onClick = state.onRetryClick) {
                                Text(text = stringResource(id = R.string.action_retry))
                            }
                        }
                    }
                }

                ListScreenState.Loading -> {
                    val loadingDescription = stringResource(id = R.string.loading)
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.semantics {
                                contentDescription = loadingDescription
                            }
                        )
                    }
                }
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
                        id = index.toString(),
                        breedName = index.toString(),
                        imageReference = ImageReference(index.toString()),
                        favoriteState = MutableStateFlow(index % 2 == 0),
                        onFavoriteClick = {},
                    )
                }.toPersistentList(),
                onSearchTextChanged = {}
            ),
        )
    }
}
