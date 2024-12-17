package com.example.pokemonproject.data.repository

import com.example.pokemonproject.data.network.DTO.PokemonDTO
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
    override suspend fun fetchPokemonData(pokemonID: Int, param: (PokemonDTO) -> Unit): PokemonDTO {
        val response: Response<Pokemon> = pokemonApi.getPokemon(pokemonID)
        val characteristicResponse:Response<ResponseModel> = pokemonApi.getCharacteristic(1)
        if (response.isSuccessful) {
            val pokemon = response.body() ?: throw Exception("Empty response body")
            var des:String=" ";
            if(pokemonID in 1..30){
                des=fetchCharacteristic(pokemonID);
            }
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
                description = des
            )
            param(pokemonDTO)
            return pokemonDTO
        } else {
            throw Exception("Failed to fetch data: ${response.errorBody()?.string()}")
        }
    }

    override suspend fun fetchPokemonList(): List<PokemonDTO> = coroutineScope {
        val pokemonList = (1..151).map { pokemonID ->
            async {
                fetchPokemonData(pokemonID) { _ ->
                }
            }
        }.awaitAll()
        pokemonList
    }
    override   suspend fun   fetchCharacteristic(id:Int):String{
         val characteristicResponse:Response<ResponseModel> = pokemonApi.getCharacteristic(id)
         val characteristic= characteristicResponse.body()?:ResponseModel(listOf())
         return characteristic.descriptions[7].description;
    }
}