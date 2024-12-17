package com.example.pokemonproject.domain.DTO

data class PokemonDTO(
    val id: Int,
    val name: String,
    val types: List<String>,
    val sprites: String
)