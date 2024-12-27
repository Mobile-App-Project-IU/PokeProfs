package com.example.pokemonproject.data.Room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pokemonproject.data.Room.Entity.ElementEntity

@Dao
interface ElementDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElement(element: ElementEntity)

    @Query("SELECT * FROM element_table WHERE id = :id")
    suspend fun getElementForPokemon(id: Int): List<ElementEntity>
}