package com.example.pokemonproject.data.repository

import androidx.room.Entity
import com.example.pokemonproject.Pokemon
import com.example.pokemonproject.data.Room.ElementDAO
import com.example.pokemonproject.data.Room.ElementEntity
import com.example.pokemonproject.data.Room.PokemonDAO
import com.example.pokemonproject.data.Room.PokemonEntity
import com.example.pokemonproject.data.network.PokemonApi
import com.example.pokemonproject.domain.DTO.PokemonDTO
import com.example.pokemonproject.domain.model.getTypeNames
import com.example.pokemonproject.domain.repository.PokemonRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import retrofit2.Response

import javax.inject.Inject

/*
class PokemonRepositoryImpl @Inject constructor(
    private val pokemonApi: PokemonApi,
    private val pokemonDao: PokemonDAO,
    private val elementDao: ElementDAO
) : PokemonRepository {
    override suspend fun fetchPokemonData(pokemonID: Int, param: (PokemonDTO) -> Unit): PokemonDTO {
        val response = pokemonApi.getPokemon(pokemonID)
        if (response.isSuccessful) {
            val pokemon = response.body() ?: throw Exception("Empty response body")
            val pokemonDTO = PokemonDTO(
                pokemon.id,
                pokemon.name,
                pokemon.types.map { it.type.name },
                pokemon.sprites.front_default
            )
            param(pokemonDTO)
            return pokemonDTO
        } else {
            throw Exception("Failed to fetch data: ${response.errorBody()?.string()}")
        }
    }

    override suspend fun fetchPokemonList(): List<PokemonDTO> = coroutineScope {
        val cachedPokemon = pokemonDao.getAllPokemon()
        if (cachedPokemon.isNotEmpty()) {
            return@coroutineScope cachedPokemon.map { PokemonEntity ->
                val elements = elementDao.getElementForPokemon(id = PokemonEntity.id).map { it.elementType }
                PokemonDTO(
                    id = PokemonEntity.id,
                    name = PokemonEntity.name,
                    types = elements,
                    sprites = PokemonEntity.imageUrl
                )
            }
        }
        val pokemonList = (1..151).map { pokemonID ->
            async {
                fetchPokemonData(pokemonID) { pokemonDTO ->
                    println(pokemonDTO.types) // Log the types
                }
            }
        }.awaitAll()
        pokemonList
    }
}
*/
class PokemonRepositoryImpl @Inject constructor(
    private val pokemonApi: PokemonApi,
    private val pokemonDao: PokemonDAO,
    private val elementDao: ElementDAO
) : PokemonRepository {

    override suspend fun fetchPokemonData(pokemonID: Int, param: (PokemonDTO) -> Unit): PokemonDTO {
        val response = pokemonApi.getPokemon(pokemonID)
        if (response.isSuccessful) {
            val pokemon = response.body() ?: throw Exception("Empty response body")
            val pokemonEntity = PokemonEntity(
                pokemon.id,
                pokemon.name,
                pokemon.types.map { it.type.name }.toString(),
                pokemon.sprites.front_default
            )

            // Save Pokémon to database
            pokemonDao.insertPokemon(pokemonEntity)
            // Save elements to database
            val elementEntities = pokemon.types.map { typeSlot ->
                ElementEntity(
                    id = pokemon.id,
                    elementType = typeSlot.type.name
                )
            }
            elementEntities.forEach { elementDao.insertElement(it) }
            return PokemonDTO(pokemon.id, pokemon.name, pokemon.getTypeNames(), pokemon.sprites.front_default)
        } else {
            throw Exception("Failed to fetch data")
        }
    }

    override suspend fun fetchPokemonList(): List<PokemonDTO> = coroutineScope {
        val cachedPokemon = pokemonDao.getAllPokemon()
        if (cachedPokemon.isNotEmpty()) {
            return@coroutineScope cachedPokemon.map { PokemonEntity ->
                val elements = elementDao.getElementForPokemon(id = PokemonEntity.id).map { it.elementType }
                PokemonDTO(
                    id = PokemonEntity.id,
                    name = PokemonEntity.name,
                    types = elements,
                    sprites = PokemonEntity.imageUrl
                )
            }
        }
        val pokemonList = (1..151).map { pokemonID ->
            async {
                fetchPokemonData(pokemonID) { pokemonDTO ->
                    println(pokemonDTO.types) // Log the types
                }
            }
        }.awaitAll()
        pokemonList
    }
}


