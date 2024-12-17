package com.example.pokemonproject.data.Room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/*
@Dao
interface PokemonDao {
    @Query("SELECT * FROM pokemon_table")
    suspend fun getAllPokemon(): List<PokemonEntity>
}*/

@Dao
interface PokemonDAO{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(pokemon: PokemonEntity)

    @Query("SELECT * FROM pokemon_table")
    suspend fun getAllPokemon(): List<PokemonEntity>
}
