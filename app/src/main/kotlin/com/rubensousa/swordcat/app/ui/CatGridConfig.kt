package com.rubensousa.swordcat.app.ui

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.ui.unit.dp

object CatGridConfig {

    fun getCells(): GridCells {
        return GridCells.Adaptive(minSize = 150.dp)
    }

}
