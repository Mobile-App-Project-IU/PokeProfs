package com.example.pokemonproject.data.Room.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "example_table")
data class PokemonEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String
)
