package com.example.pokemonproject.data.repository

import com.example.pokemonproject.domain.DTO.PokemonDTO
import com.example.pokemonproject.data.network.PokemonApi
import com.example.pokemonproject.domain.model.Pokemon
import com.example.pokemonproject.domain.model.ResponseModel
import com.example.pokemonproject.domain.repository.PokemonRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import retrofit2.Response
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(private val pokemonApi: PokemonApi) :
    PokemonRepository {
        //View model use this function to get the pokemon data
        override suspend fun getPokemonData(): List<PokemonDTO> {
            val pokemonList = getLocalPokemonData()
//            if (pokemonList.isNotEmpty()) {
//                return pokemonList
//            }
            return fetchPokemonData()
        }
    //fetch online data
    override suspend fun fetchPokemonData(): List<PokemonDTO> = coroutineScope {
        (1..151).map { pokemonID ->
            async {
                val response: Response<Pokemon> = pokemonApi.getPokemon(pokemonID)
                if (response.isSuccessful) {
                    val pokemon = response.body() ?: throw Exception("Empty response body")
                    val description = " " // You can modify this to include actual description logic if needed
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
                    insertPokemon(pokemonDTO)
                    pokemonDTO
                } else {
                    throw Exception("Failed to fetch data: ${response.errorBody()?.string()}")
                }
            }
        }.awaitAll()
    }
    override   suspend fun   fetchCharacteristic(id:Int):String{
        val characteristicResponse:Response<ResponseModel> = pokemonApi.getCharacteristic(id)
        val characteristic= characteristicResponse.body()?:ResponseModel(listOf())
        return characteristic.descriptions[7].description;
    }
    //Room database insert pokemon
    override suspend fun insertPokemon(pokemonDTO: PokemonDTO) {

    }
    //Get local pokemon data
    override suspend fun getLocalPokemonData(): List<PokemonDTO> {
        return emptyList()
    }
    //Get local pokemon data by id
    override suspend fun getLocalPokemonDataById(id: Int): PokemonDTO {
        return PokemonDTO()
    }


}