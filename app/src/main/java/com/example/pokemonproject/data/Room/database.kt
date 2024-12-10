package com.example.pokemonproject.data.Room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pokemonproject.data.Room.DAO.PokemonDao
import com.example.pokemonproject.data.Room.Entity.PokemonEntity

@Database(entities = [PokemonEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
}
