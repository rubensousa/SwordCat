package com.rubensousa.swordcat.backend.internal

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CatRemoteModel(
    @SerialName("id") val id: String,
    @SerialName("name") val breedName: String,
    @SerialName("temperament") val temperament: String,
    @SerialName("origin") val origin: String,
    @SerialName("description") val description: String,
    @SerialName("life_span") val lifeSpan: String,
    @SerialName("reference_image_id") val imageId: String,
)
