package com.example.pokemonproject.data.Room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "element_table",
    foreignKeys = [
        ForeignKey(
            entity = PokemonEntity::class,
            parentColumns = ["id"],
            childColumns = ["elementId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ElementEntity(
    @PrimaryKey(autoGenerate = true)
    val elementId: Int = 0,
    val id: Int,
    val elementType: String
)