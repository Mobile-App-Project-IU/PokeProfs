package com.example.pokemonproject.data.Room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pokemonproject.data.Room.Entity.PokemonEntity
import kotlinx.coroutines.flow.Flow

// Data Access Object (DAO) for Pokémon-related database operations
@Dao
interface PokemonDAO {

    // Inserts a Pokémon entity into the database
    // Replaces the existing entry if a conflict occurs (e.g., same primary key)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pokemon: PokemonEntity)

    // Fetches all Pokémon entities from the database table 'pokemon_table'
    // Returns a Flow to observe real-time data updates
    @Query("SELECT * FROM pokemon_table")
    fun getAll(): Flow<List<PokemonEntity>>
}
