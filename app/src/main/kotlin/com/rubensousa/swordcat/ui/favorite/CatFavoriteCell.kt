package com.rubensousa.swordcat.ui.favorite

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.rubensousa.swordcat.ui.cat.CatCellContainer
import com.rubensousa.swordcat.ui.image.ImageReference
import com.rubensousa.swordcat.ui.theme.SwordCatTheme

@Composable
fun CatFavoriteCell(
    name: String,
    imageReference: ImageReference?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CatCellContainer(
        name = name,
        imageReference = imageReference,
        onClick = onClick,
        modifier = modifier
    )
}

@Preview
@Composable
private fun PreviewCatFavoriteCell() {
    SwordCatTheme {
        CatFavoriteCell(
            name = "Some cat",
            imageReference = ImageReference("id"),
            onClick = {}
        )
    }
}
