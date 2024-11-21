package com.example.myapplication



data class Pokemon(
    val name: String,
    val id: Int,
    val element: List<String> = emptyList(),
)

