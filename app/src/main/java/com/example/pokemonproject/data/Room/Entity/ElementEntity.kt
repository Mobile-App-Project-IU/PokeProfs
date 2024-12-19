package com.example.pokemonproject.data.Room.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "element_table")
data class ElementEntity(
    @PrimaryKey(autoGenerate = true)
    val elementId: Int = 0,
    val id: Int,
    val elementType: String
)