package com.example.pokemonproject.data.network.DTO

data class PokemonDTO(
    val id: Int,
    val name: String,
    val types: List<String>,
    val sprites: String
)