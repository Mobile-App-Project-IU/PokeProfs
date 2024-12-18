package com.example.pokemonproject.domain.repository

import com.example.pokemonproject.domain.DTO.PokemonDTO

interface PokemonRepository {
    suspend fun fetchPokemonData(): List<PokemonDTO>
    suspend fun  fetchCharacteristic(id:Int):String
    suspend fun getPokemonData():List<PokemonDTO>
    suspend fun  insertPokemon(pokemonDTO: PokemonDTO):Unit
    suspend fun  getLocalPokemonData():List<PokemonDTO>
    suspend fun  getLocalPokemonDataById(id:Int):PokemonDTO

}