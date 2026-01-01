package com.rubensousa.swordcat.app.favorite

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.rubensousa.swordcat.app.image.ImageReference
import com.rubensousa.swordcat.app.theme.SwordCatTheme
import com.rubensousa.swordcat.app.ui.CatCellLayout

@Composable
fun CatFavoriteCell(
    name: String,
    imageReference: ImageReference?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CatCellLayout(
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
