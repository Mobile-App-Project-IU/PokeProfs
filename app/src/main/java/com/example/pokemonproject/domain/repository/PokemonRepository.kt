package com.example.pokemonproject.domain.repository

import com.example.pokemonproject.domain.DTO.PokemonDTO

// Interface for accessing Pokémon data from different sources (remote and local)
interface PokemonRepository {

    // Get the list of Pokémon data (DTOs) from a local source (e.g., database)
    suspend fun getPokemonData():List<PokemonDTO>

    // Fetch Pokémon data from a remote source, such as an API.
    suspend fun fetchPokemonData(): List<PokemonDTO>

    // Fetch a specific characteristic (e.g., description) of a Pokémon by its ID.
    suspend fun fetchCharacteristic(id: Int): String

    // Get Pokémon data from a local source (e.g., database).
    suspend fun getLocalPokemonData(): List<PokemonDTO>

    // Insert a new Pokémon into the local data source (e.g., database).
    suspend fun insertPokemon(pokemonDTO: PokemonDTO)

    // Retrieve a specific Pokémon's data from the local data source by its ID.
    suspend fun getLocalPokemonDataById(id: Int): PokemonDTO
}
