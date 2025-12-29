package com.rubensousa.swordcat.ui.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rubensousa.swordcat.ui.detail.DetailNavKey
import com.rubensousa.swordcat.ui.navigation.LocalNavigator

@Composable
fun ListScreen() {
    val navigator = LocalNavigator.current
    Scaffold(
        modifier = Modifier.fillMaxSize()
            .systemBarsPadding(),
    ) { innerPadding ->
        Button(
            onClick = {
                navigator.navigateTo(DetailNavKey("id"))
            }
        ) {
            Text("Go to detail")
        }
    }
}
