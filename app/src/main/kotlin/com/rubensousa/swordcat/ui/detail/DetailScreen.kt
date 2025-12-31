package com.rubensousa.swordcat.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.rubensousa.swordcat.R
import com.rubensousa.swordcat.ui.StringResource
import com.rubensousa.swordcat.ui.image.ImageReference
import com.rubensousa.swordcat.ui.navigation.LocalNavigator
import com.rubensousa.swordcat.ui.text
import com.rubensousa.swordcat.ui.theme.SwordCatTheme
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun DetailScreen(
    id: String,
) {
    val viewModel = hiltViewModel<DetailViewModel, DetailViewModel.Factory>(
        creationCallback = { factory -> factory.create(id) }
    )
    val state by viewModel.getUiState().collectAsStateWithLifecycle()
    DetailScreen(state = state)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    state: DetailScreenState,
) {
    val navigator = LocalNavigator.current
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (state is DetailScreenState.Content) {
                            state.breedName
                        } else {
                            ""
                        }
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navigator.navigateBack()
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_navigation_back),
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    if (state is DetailScreenState.Content) {
                        val isFavorite by state.isFavorite.collectAsStateWithLifecycle()
                        val favoriteDescription = if (isFavorite) {
                            stringResource(R.string.favorite_on)
                        } else {
                            stringResource(R.string.favorite_off)
                        }
                        IconButton(
                            onClick = state.onFavoriteClick
                        ) {
                            Icon(
                                painter = if (isFavorite) {
                                    painterResource(R.drawable.ic_state_favorite_on)
                                } else {
                                    painterResource(R.drawable.ic_state_favorite_off)
                                },
                                contentDescription = favoriteDescription
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (state) {
                is DetailScreenState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is DetailScreenState.Error -> {
                    Text(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        text = state.message.text()
                    )
                }

                is DetailScreenState.Content -> {
                    DetailContent(state = state)
                }
            }
        }
    }
}

@Composable
private fun DetailContent(state: DetailScreenState.Content) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth(),
            model = state.imageReference,
            contentScale = ContentScale.FillWidth,
            contentDescription = null
        )
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            state.infoItems.forEach { item ->
                DetailInfoItemRow(item)
            }
        }
    }
}

@Composable
private fun DetailInfoItemRow(item: DetailInfoItem) {
    val label = item.label.text()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .semantics {
                contentDescription = label
            }) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = item.value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview
@Composable
private fun PreviewDetailScreenContent() {
    SwordCatTheme {
        DetailScreen(
            state = DetailScreenState.Content(
                breedName = "Abyssinian",
                infoItems = persistentListOf(
                    DetailInfoItem(
                        label = StringResource.fromString("Origin"),
                        value = "Egypt"
                    ),
                    DetailInfoItem(
                        label = StringResource.fromString("Description"),
                        value = "The Abyssinian is a breed of domestic short-haired cat with a distinctive \"ticked\" tabby coat."
                    )
                ),
                imageReference = ImageReference("id"),
                isFavorite = MutableStateFlow(true),
                onFavoriteClick = {}
            )
        )
    }
}
