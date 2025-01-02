package com.example.pokemonproject.domain.model

import com.example.pokemonproject.domain.DTO.PokemonDTO
import com.google.gson.annotations.SerializedName

// Represents a Pokémon with its various attributes (types, stats, sprites, etc.)
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

// Represents a Pokémon stat with base stat, effort, and associated stat details
data class PokemonStat(
    @SerializedName("base_stat") val baseStat: Int, // Base stat value
    val effort: Int, // Effort points gained
    val stat: PokemonStatX // Stat details (name and URL)
)

// Represents the details of a stat (e.g., "attack" or "defense")
data class PokemonStatX(
    val name: String,
    val url: String
)

// Represents the types of a Pokémon (e.g., Fire, Water)
data class PokemonType(
    val id: Int,
    val type: Type
)

// Represents the type of a Pokémon (e.g., "fire" or "water")
data class Type(
    val name: String
)

// Represents the sprites (images) of a Pokémon, such as front-facing sprites
data class Sprites(
    val front_default: String
)

// Extension function to retrieve the names of all types of a Pokémon
fun Pokemon.getTypeNames(): List<String> = types.map { it.type.name }

// Enum class representing the possible states of Pokémon data loading
enum class PokemonStatus {
    LOADING,
    SUCCESS,
    ERROR,
    INIT
}

// Represents the state of Pokémon data, including list and individual Pokémon DTOs
data class PokemonState(
    var pokemonList: List<PokemonDTO> = emptyList(),
    var status: PokemonStatus = PokemonStatus.LOADING,
    var pokemon: PokemonDTO? = null
)

//data class Pokemon(val name:String,val abilities:List<PokemonAbility>)
//data class PokemonAbility(val is_hidden:Boolean, val slot:Int, val ability:NamedAPIResource)
//data class NamedAPIResource(val name:String, val url: String)
