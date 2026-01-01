package com.rubensousa.swordcat.backend.internal

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ImageRemoteModel(
    @SerialName("url") val url: String
)
