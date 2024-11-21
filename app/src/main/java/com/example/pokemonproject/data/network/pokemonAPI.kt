package com.example.pokemonproject.data.network

import com.example.pokemonproject.domain.model.Pokemon
import com.example.pokemonproject.domain.model.PokemonList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonApi {
    @GET("pokemon/{name}")
    suspend fun getPokemon(@Path("name") name: String): Response<Pokemon>
    @GET("pokemon?offset=20&limit=20")
    suspend fun getPokemonList(): Response<PokemonList>
}
