package com.example.pokemonproject.data.Room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [PokemonEntity::class, ElementEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDAO
    abstract fun elementDao(): ElementDAO
}