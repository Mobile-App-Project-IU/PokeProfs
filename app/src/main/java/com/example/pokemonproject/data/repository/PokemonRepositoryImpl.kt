package com.example.pokemonproject.data.repository

import com.example.pokemonproject.data.Room.ElementDAO
import com.example.pokemonproject.data.Room.Entity.ElementEntity
import com.example.pokemonproject.data.Room.Entity.PokemonEntity
import com.example.pokemonproject.data.Room.PokemonDAO
import com.example.pokemonproject.domain.DTO.PokemonDTO
import com.example.pokemonproject.data.network.PokemonApi
import com.example.pokemonproject.domain.model.Pokemon
import com.example.pokemonproject.domain.model.ResponseModel
import com.example.pokemonproject.domain.repository.PokemonRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import retrofit2.Response
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val pokemonApi: PokemonApi,
    private val pokemonDao: PokemonDAO,
    private val elementDao: ElementDAO
) : PokemonRepository {

    // Function to get Pokémon data, first checking locally and then remotely if necessary
    override suspend fun getPokemonData(): List<PokemonDTO> {
        // Check for local Pokémon data first
        val pokemonList = getLocalPokemonData()
        if (pokemonList.isNotEmpty()) {
            return pokemonList // Return local data if available
        }
        return fetchPokemonData() // Fetch remote data if no local data
    }

    // Function to fetch Pokémon data from the remote API
    override suspend fun fetchPokemonData(): List<PokemonDTO> = coroutineScope {
        (1..151).map { pokemonID ->
            async {
                val response: Response<Pokemon> = pokemonApi.getPokemon(pokemonID)
                if (response.isSuccessful) {
                    val pokemon = response.body() ?: throw Exception("Empty response body")
                    val description = " " // Placeholder for Pokémon description (to be expanded)
                    val pokemonDTO = PokemonDTO(
                        id = pokemon.id,
                        name = pokemon.name,
                        types = pokemon.types.map { it.type.name },
                        sprites = pokemon.sprites.front_default,
                        order = pokemon.order.toString(),
                        height = pokemon.height.toString(),
                        weight = pokemon.weight.toString(),
                        hp = pokemon.stats[0].baseStat.toString(),
                        attack = pokemon.stats[1].baseStat.toString(),
                        defense = pokemon.stats[2].baseStat.toString(),
                        special_attack = pokemon.stats[3].baseStat.toString(),
                        special_defense = pokemon.stats[4].baseStat.toString(),
                        speed = pokemon.stats[5].baseStat.toString(),
                        description = description
                    )
                    insertPokemon(pokemonDTO) // Save the fetched data in the local database
                    pokemonDTO // Return the mapped DTO
                } else {
                    throw Exception("Failed to fetch data: ${response.errorBody()?.string()}")
                }
            }
        }.awaitAll() // Await all async tasks to be completed
    }

    // Function to fetch a Pokémon's characteristics (such as description)
    override suspend fun fetchCharacteristic(id: Int): String {
        val characteristicResponse: Response<ResponseModel> = pokemonApi.getCharacteristic(id)
        val characteristic = characteristicResponse.body() ?: ResponseModel(listOf())
        return characteristic.descriptions[7].description // Return the 8th description (example)
    }

    // Insert a Pokémon into the local Room database
    override suspend fun insertPokemon(pokemonDTO: PokemonDTO) {
        val pokemonEntity = PokemonEntity(
            id = pokemonDTO.id,
            name = pokemonDTO.name,
            sprites = pokemonDTO.sprites,
            order = pokemonDTO.order,
            height = pokemonDTO.height,
            weight = pokemonDTO.weight,
            hp = pokemonDTO.hp,
            attack = pokemonDTO.attack,
            defense = pokemonDTO.defense,
            special_attack = pokemonDTO.special_attack,
            special_defense = pokemonDTO.special_defense,
            speed = pokemonDTO.speed,
            description = pokemonDTO.description
        )
        pokemonDao.insert(pokemonEntity) // Insert Pokémon entity into the database

        // Insert elements (types) into the Element DAO
        pokemonDTO.types.forEach { elementType ->
            val elementEntity = ElementEntity(
                id = pokemonDTO.id,
                elementType = elementType
            )
            elementDao.insertElement(elementEntity) // Insert element into the database
        }
    }

    // Retrieve local Pokémon data from the Room database
    override suspend fun getLocalPokemonData(): List<PokemonDTO> {
        val pokemonEntities = pokemonDao.getAll().firstOrNull() ?: emptyList()
        return pokemonEntities.map { pokemonEntity ->
            val elements = elementDao.getElementForPokemon(pokemonEntity.id).map { it.elementType }
            PokemonDTO(
                id = pokemonEntity.id,
                name = pokemonEntity.name,
                types = elements,
                sprites = pokemonEntity.sprites,
                order = pokemonEntity.order,
                height = pokemonEntity.height,
                weight = pokemonEntity.weight,
                hp = pokemonEntity.hp,
                attack = pokemonEntity.attack,
                defense = pokemonEntity.defense,
                special_attack = pokemonEntity.special_attack,
                special_defense = pokemonEntity.special_defense,
                speed = pokemonEntity.speed,
                description = pokemonEntity.description
            )
        }
    }

    // Retrieve local Pokémon data for a specific Pokémon ID from the Room database
    override suspend fun getLocalPokemonDataById(id: Int): PokemonDTO {
        val pokemonEntity = pokemonDao.getAll().firstOrNull()?.find { it.id == id }
            ?: throw Exception("Pokemon with ID $id not found")

        val elements = elementDao.getElementForPokemon(id).map { it.elementType }

        return PokemonDTO(
            id = pokemonEntity.id,
            name = pokemonEntity.name,
            types = elements,
            sprites = pokemonEntity.sprites,
            order = pokemonEntity.order,
            height = pokemonEntity.height,
            weight = pokemonEntity.weight,
            hp = pokemonEntity.hp,
            attack = pokemonEntity.attack,
            defense = pokemonEntity.defense,
            special_attack = pokemonEntity.special_attack,
            special_defense = pokemonEntity.special_defense,
            speed = pokemonEntity.speed,
            description = pokemonEntity.description
        )
    }
}
