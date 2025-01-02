package com.example.pokemonproject.data.network

import com.example.pokemonproject.domain.model.ResponseModel
import com.example.pokemonproject.domain.model.Pokemon
import com.example.pokemonproject.domain.model.PokemonList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

// Interface for interacting with the Pokémon API via Retrofit
interface PokemonApi {

    // Fetches details of a specific Pokémon by its ID
    @GET("pokemon/{id}")
    suspend fun getPokemon(
        @Path("id") id: Int // ID of the Pokémon
    ): Response<Pokemon>

    // Fetches a paginated list of Pokémon (default offset and limit: 20)
    @GET("pokemon?offset=20&limit=20")
    suspend fun getPokemonList(): Response<PokemonList>

    // Fetches the characteristic of a specific Pokémon by its ID
    @GET("characteristic/{pokemonId}")
    suspend fun getCharacteristic(
        @Path("pokemonId") pid: Int // Pokémon ID for characteristic retrieval
    ): Response<ResponseModel>
}
