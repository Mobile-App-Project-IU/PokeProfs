package com.example.pokemonproject.domain.repository

import com.example.pokemonproject.data.network.DTO.PokemonDTO

interface PokemonRepository {
    suspend fun fetchPokemonData(pokemonID: Int,param: (PokemonDTO) -> Unit = {}): PokemonDTO
    suspend fun fetchPokemonList():List<PokemonDTO>
    suspend fun  fetchCharacteristic(id:Int):String
}