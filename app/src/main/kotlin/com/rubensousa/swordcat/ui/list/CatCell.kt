package com.rubensousa.swordcat.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.rubensousa.swordcat.R
import com.rubensousa.swordcat.ui.image.ImageReference
import com.rubensousa.swordcat.ui.theme.SwordCatTheme

@Composable
fun CatCell(
    name: String,
    imageReference: ImageReference?,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CatCellContainer(
        name = name,
        imageReference = imageReference,
        onClick = onClick,
        modifier = modifier,
        actionSlot = {
            val favoriteDescription = if (isFavorite) {
                stringResource(R.string.favorite_on)
            } else {
                stringResource(R.string.favorite_off)
            }
            IconButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .semantics {
                        contentDescription = favoriteDescription
                    },
                onClick = onFavoriteClick
            ) {
                Icon(
                    painter = if (isFavorite) {
                        painterResource(R.drawable.ic_state_favorite_on)
                    } else {
                        painterResource(R.drawable.ic_state_favorite_off)
                    },
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = null
                )
            }
        }
    )
}

@Composable
fun CatCellContainer(
    name: String,
    imageReference: ImageReference?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    actionSlot: @Composable BoxScope.() -> Unit = {}
) {
    Card(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .semantics {
                    contentDescription = name
                }
                .clickable {
                    onClick()
                }
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = imageReference,
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .drawWithContent {
                        drawRect(Color.Black.copy(alpha = 0.2f))
                        drawContent()
                    }
            ) {
                actionSlot()
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.DarkGray)
                        .padding(8.dp)
                        .align(Alignment.BottomStart),
                    text = name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewCatCell() {
    SwordCatTheme {
        CatCell(
            name = "Some cat",
            imageReference = ImageReference("id"),
            isFavorite = true,
            onFavoriteClick = {},
            onClick = {}
        )
    }
}
