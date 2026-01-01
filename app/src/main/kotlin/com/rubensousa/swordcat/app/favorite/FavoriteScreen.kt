package com.rubensousa.swordcat.app.favorite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rubensousa.swordcat.R
import com.rubensousa.swordcat.app.detail.DetailNavKey
import com.rubensousa.swordcat.app.image.ImageReference
import com.rubensousa.swordcat.app.navigation.LocalNavigator
import com.rubensousa.swordcat.app.theme.SwordCatTheme
import kotlinx.collections.immutable.persistentListOf

@Composable
fun FavoriteScreen() {
    val viewModel = hiltViewModel<FavoriteViewModel>()
    FavoriteScreen(
        state = viewModel.uiState.collectAsStateWithLifecycle().value,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    state: FavoriteScreenState,
) {
    val navigator = LocalNavigator.current
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.navigation_favorites))
                }
            )
        },
        bottomBar = {
            Card(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    text = stringResource(
                        id = R.string.favorite_lifespan,
                        state.averageLifespan
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
        ) {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Adaptive(minSize = 150.dp),
                contentPadding = PaddingValues(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(state.items) { item ->
                    CatFavoriteCell(
                        name = item.breedName,
                        imageReference = item.imageReference,
                        onClick = {
                            navigator.navigateTo(DetailNavKey(item.id))
                        },
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewFavoriteScreen() {
    SwordCatTheme {
        FavoriteScreen(
            state = FavoriteScreenState(
                items = persistentListOf(
                    CatFavoriteItem(
                        id = "1",
                        breedName = "Abyssinian",
                        imageReference = ImageReference("1"),
                    ),
                    CatFavoriteItem(
                        id = "2",
                        breedName = "Bengal",
                        imageReference = ImageReference("2"),
                    )
                ),
                averageLifespan = "14.5"
            ),
        )
    }
}
