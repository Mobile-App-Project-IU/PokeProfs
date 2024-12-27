package com.example.pokemonproject.data.Room.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_table")
data class PokemonEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int =0,
    val name: String="",
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
    var description:String=""
)
