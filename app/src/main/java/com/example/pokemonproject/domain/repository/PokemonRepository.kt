package com.example.pokemonproject.domain.repository

import com.example.pokemonproject.data.network.DTO.PokemonDTO

interface PokemonRepository {
    suspend fun fetchPokemonData(pokemonID: Int): PokemonDTO
    suspend fun fetchPokemonList():List<PokemonDTO>
}