package com.example.pokemonproject.domain.repository

import com.example.pokemonproject.domain.DTO.PokemonDTO

interface PokemonRepository {
    suspend fun fetchPokemonData(pokemonID: Int,param: (PokemonDTO) -> Unit = {}): PokemonDTO
    suspend fun fetchPokemonList():List<PokemonDTO>
    suspend fun  fetchCharacteristic(id:Int):String
    suspend fun  insertPokemon(pokemonDTO: PokemonDTO):Unit
    suspend fun  getPokemonList():List<PokemonDTO>
}