package com.example.pokemonproject.domain.model

// Represents the response structure that holds a collection of descriptions
data class ResponseModel(
    val descriptions: List<Description> = emptyList() // Default empty list to prevent nullability
)

// Represents an individual description, such as a Pokémon's flavor text or feature
data class Description(
    val description: String = "" // Default empty string to prevent nullability
)
