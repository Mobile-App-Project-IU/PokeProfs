package com.example.pokemonproject.domain.model

data class Pokemon(
    val id: Int,
    val name: String,
    val types: List<TypeSlot>,
    val sprites: Sprites
)
data class TypeSlot(
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