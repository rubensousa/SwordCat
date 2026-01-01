package com.rubensousa.swordcat.domain

data class Cat(
    val id: String,
    val breedName: String,
    val origin: String,
    val temperament: String,
    val description: String,
    val imageId: String?,
    val lifespan: IntRange
)
