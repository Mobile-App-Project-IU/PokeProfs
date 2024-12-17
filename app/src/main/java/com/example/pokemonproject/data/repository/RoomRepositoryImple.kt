package com.example.pokemonproject.data.repository

import com.example.pokemonproject.domain.DTO.PokemonDTO
import com.example.pokemonproject.domain.repository.RoomRepository

class RoomRepositoryImple: RoomRepository {
    override suspend fun getPokemon(id: Int): PokemonDTO {
        TODO("Not yet implemented")
    }

    override suspend fun listPokemon(): List<PokemonDTO> {
        TODO("Not yet implemented")
    }

    override suspend fun insertPokemon(pokemonDTO: PokemonDTO) {
        TODO("Not yet implemented")
    }
}