package com.example.pokemonproject.domain.model

data class Team(
    val id: Int = 0,                // Unique ID for the team
    val name: String,               // Team name
    val members: List<String>     // 6 Pokemon slots
)
