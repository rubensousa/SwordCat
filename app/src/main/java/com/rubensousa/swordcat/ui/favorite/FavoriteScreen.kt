package com.rubensousa.swordcat.ui.favorite

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rubensousa.swordcat.ui.detail.DetailNavKey
import com.rubensousa.swordcat.ui.navigation.LocalNavigator

@Composable
fun FavoriteScreen() {
    val navigator = LocalNavigator.current
    Scaffold(
        modifier = Modifier.fillMaxSize()
            .systemBarsPadding(),
    ) { innerPadding ->
        Button(
            modifier = Modifier.padding(innerPadding),
            onClick = {
                navigator.navigateTo(DetailNavKey("id"))
            }
        ) {
            Text("See detail")
        }
    }
}
