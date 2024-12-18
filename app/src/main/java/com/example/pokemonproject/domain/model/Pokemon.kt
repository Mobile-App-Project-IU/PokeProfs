package com.example.pokemonproject.domain.model

import com.example.pokemonproject.domain.DTO.PokemonDTO
import com.google.gson.annotations.SerializedName

data class Pokemon(
    val id: Int,
    val name: String,
    val types: List<PokemonType>,
    val sprites: Sprites,
    val order: Int,
    val height: Int,
    val weight: Int,
    val stats: List<PokemonStat>,
)
data class PokemonStat(
    @SerializedName("base_stat")
    val baseStat: Int,
    val effort: Int,
    val stat: PokemonStatX
)

data class PokemonStatX(
    val name: String,
    val url: String
)
data class PokemonType(
    val id:Int,
    val type: Type
)
data class Type(
    val name: String,
)

data class Sprites(
    val front_default: String
)

fun Pokemon.getTypeNames(): List<String> {
    return types.map { it.type.name }
}

// Sample usage of the model
enum class PokemonStatus {
    LOADING,
    SUCCESS,
    ERROR,
    INIT
}

data class PokemonState(
    var pokemonList: List<PokemonDTO> =emptyList(),//data
    var status: PokemonStatus = PokemonStatus.LOADING,
    var pokemon: PokemonDTO? =null

    )


//data class Pokemon(val name:String,val abilities:List<PokemonAbility>)
//data class PokemonAbility(val is_hidden:Boolean, val slot:Int, val ability:NamedAPIResource)
//data class NamedAPIResource(val name:String, val url: String)
