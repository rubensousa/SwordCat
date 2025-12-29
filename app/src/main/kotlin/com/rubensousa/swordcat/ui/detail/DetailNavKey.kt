package com.rubensousa.swordcat.ui.detail

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class DetailNavKey(
    val id: String,
) : NavKey
