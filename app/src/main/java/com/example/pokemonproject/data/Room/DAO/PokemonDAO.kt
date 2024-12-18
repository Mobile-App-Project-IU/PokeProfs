package com.example.pokemonproject.data.Room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pokemonproject.data.Room.Entity.PokemonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(example: PokemonEntity)
    @Query("SELECT * FROM example_table")
    fun getAll(): Flow<List<PokemonEntity>>
}
