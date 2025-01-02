package com.example.pokemonproject.domain.model

// Data class representing a list of Pokémon, where each Pokémon is represented by its name
data class PokemonList(
    val results: List<PokemonName> // A list of Pokémon names in the form of PokemonName objects
)

// Data class to represent the name of a single Pokémon
data class PokemonName(
    val name: String // The name of the Pokémon (e.g., "Pikachu", "Charmander")
)

// Extension function on PokemonList to get a list of Pokémon names
fun PokemonList.getPokemonNames(): List<String> {
    // Transform the results list into a list of Pokémon names (strings) by mapping each PokémonName to its name property
    return results.map { it.name }
}
