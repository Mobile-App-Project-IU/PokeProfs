package com.example.pokemonproject.data.Room.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pokemonproject.domain.model.Pokemon
import com.example.pokemonproject.domain.model.Team
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "teams")
data class TeamEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val members: String // Store the team as a JSON string
)

fun TeamEntity.toDomain(): Team {
    val type = object : TypeToken<List<String>>() {}.type // List of strings for Pokémon names or IDs
    val membersList: List<String> = Gson().fromJson(this.members, type)
    return Team(id, name, membersList)
}


fun Team.toEntity(): TeamEntity {
    val gson = Gson()
    val membersJson = gson.toJson(this.members)
    return TeamEntity(id, name, membersJson)
}


