package com.example.pokemonproject.domain.repository

import com.example.pokemonproject.domain.DTO.PokemonDTO

interface RoomRepository {
    suspend fun  getPokemon(id:Int):PokemonDTO;
    suspend fun listPokemon():List<PokemonDTO>;
    suspend fun insertPokemon(pokemonDTO: PokemonDTO);
}