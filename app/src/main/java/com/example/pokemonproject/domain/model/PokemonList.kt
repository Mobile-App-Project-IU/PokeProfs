package com.example.pokemonproject.domain.model
data class PokemonList(
    val results: List<PokemonName>,
)
data class PokemonName(
    val name: String,
)
fun PokemonList.getPokemonNames(): List<String> {
    return results.map { it.name }
}