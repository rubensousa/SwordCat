package com.rubensousa.swordcat.app.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.rubensousa.swordcat.app.image.ImageReference
import com.rubensousa.swordcat.app.theme.SwordCatTheme

@Composable
fun CatCellLayout(
    name: String,
    imageReference: ImageReference?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    overlay: @Composable BoxScope.() -> Unit = {}
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
            val blackAlpha = remember { Color.Black.copy(alpha = 0.6f) }
            val topGradientSizePercentage = 0.5f
            val bottomGradientSizePercentage = 0.75f
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .drawWithContent {
                        drawRect(
                            Brush.verticalGradient(
                                colors = listOf(blackAlpha, Color.Transparent),
                                startY = 0f,
                                endY = this.size.height * topGradientSizePercentage
                            ),
                            size = this.size.copy(height = this.size.height * topGradientSizePercentage)
                        )
                        drawRect(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, blackAlpha),
                                startY = this.size.height * (1 - bottomGradientSizePercentage),
                                endY = this.size.height
                            ),
                            size = this.size
                        )
                        drawContent()
                    }
            ) {
                overlay()
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
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
private fun PreviewCatCellLayout() {
    SwordCatTheme {
        CatCellLayout(
            name = "Some cat",
            imageReference = ImageReference("id"),
            onClick = {}
        )
    }
}
