package com.example.pokemonproject.data.network.DTO



data class  PokemonDTO(
    val id: Int =0,
    val name: String="",
    val types: List<String> ,
    val sprites: String="",
    val order: String="",
    val height: String="",
    val weight: String="",
    val hp:String="",
    val attack:String="",
    val defense:String="",
    val special_attack:String="",
    val special_defense:String="",
    val speed:String="",
)