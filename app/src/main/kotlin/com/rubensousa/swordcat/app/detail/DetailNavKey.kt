package com.rubensousa.swordcat.app.detail

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class DetailNavKey(
    val id: String,
) : NavKey
