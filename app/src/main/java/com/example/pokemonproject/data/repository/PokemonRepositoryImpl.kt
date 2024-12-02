package com.example.pokemonproject.data.repository

import com.example.pokemonproject.domain.model.Pokemon
import com.example.pokemonproject.data.network.DTO.PokemonDTO
import com.example.pokemonproject.data.network.PokemonApi
import com.example.pokemonproject.domain.repository.PokemonRepository
import retrofit2.Response
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(private val pokemonApi: PokemonApi ) : PokemonRepository {
    override suspend fun fetchPokemonData(pokemonID: Int): PokemonDTO {
        val response: Response<Pokemon> = pokemonApi.getPokemon(pokemonID)
        if (response.isSuccessful) {
            val pokemon = response.body()!!
            return PokemonDTO(
                pokemon.id,
                pokemon.name,
                pokemon.types.map { it.type.name },
                pokemon.sprites.front_default
            )

        } else {
            throw Exception("Failed to fetch data")
        }
    }

    override suspend fun fetchPokemonList(): List<PokemonDTO> {
        val response = pokemonApi.getPokemonList()
        val pokemonList = mutableListOf<PokemonDTO>()
        if (response.isSuccessful) {

            for (pokemonID in 1..151) {
                val pokemon = fetchPokemonData(pokemonID) // Fetch full Pokémon data
                pokemonList.add(
                    pokemon
                ) // Add the DTO object to the list
            }
        } else {
            throw Exception("Failed to fetch data")
        }

        return pokemonList
    }
}