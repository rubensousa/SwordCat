package com.rubensousa.swordcat.app.list

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.rubensousa.swordcat.R
import com.rubensousa.swordcat.app.image.ImageReference
import com.rubensousa.swordcat.app.theme.SwordCatTheme
import com.rubensousa.swordcat.app.ui.CatCellLayout

@Composable
fun CatListCell(
    name: String,
    imageReference: ImageReference?,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CatCellLayout(
        name = name,
        imageReference = imageReference,
        onClick = onClick,
        modifier = modifier,
        overlay = {
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

@Preview
@Composable
private fun PreviewCatListCell() {
    SwordCatTheme {
        CatListCell(
            name = "Some cat",
            imageReference = ImageReference("id"),
            isFavorite = true,
            onFavoriteClick = {},
            onClick = {}
        )
    }
}
