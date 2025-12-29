package com.rubensousa.swordcat.ui.detail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.rubensousa.swordcat.R
import com.rubensousa.swordcat.ui.navigation.LocalNavigator
import com.rubensousa.swordcat.ui.theme.SwordCatTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    id: String,
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
                        text = "Cat",
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
                }
            )
        }
    ) { innerPadding ->
        Text(
            modifier = Modifier.padding(innerPadding),
            text = "Detail cat"
        )
    }

}

@Preview
@Composable
private fun PreviewDetailScreen() {
    SwordCatTheme {
        DetailScreen(
            id = ""
        )
    }
}