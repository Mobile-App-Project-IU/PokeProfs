package com.example.myapplication.ui.theme

import androidx.compose.ui.graphics.Color
import com.example.myapplication.R

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val PokeBallRed = Color(0xFFFF0000)
val PokeBallWhite = Color(0xFFFFFFFF)
val PokeBallBlack = Color(0xFF000000)
val PokemonYellow = Color(0xFFFFD700)
val PokemonBlue = Color(0xFF1E90FF)
val PokemonGreen = Color(0xFF32CD32)
val PokemonGray = Color(0xFF808080)

fun elementColor(type: String): Color {
    return when (type.lowercase()) {
        "normal" -> Color(0xFF9E9E9E) // Grayish neutral
        "fire" -> Color(0xFFFF5722) // Red-orange
        "water" -> Color(0xFF2196F3) // Deep blue
        "electric" -> Color(0xFFFFEB3B) // Bright yellow
        "grass" -> Color(0xFF008840) // Fresh green
        "ice" -> Color(0xFF81D4FA) // Cool light blue
        "fighting" -> Color(0xFFB71C1C) // Strong red
        "poison" -> Color(0xFF9C27B0) // Purple
        "ground" -> Color(0xFFD7C297) // Earthy tan
        "flying" -> Color(0xFF90CAF9) // Soft light blue
        "psychic" -> Color(0xFFF06292) // Light pink
        "bug" -> Color(0xFF8BC34A) // Light green
        "rock" -> Color(0xFFEDDBB6) // Brown
        "ghost" -> Color(0xFF673AB7) // Dark purple
        "dragon" -> Color(0xFF1976D2) // Strong blue
        "dark" -> Color(0xFF212121) // Deep gray/black
        "steel" -> Color(0xFFB0BEC5) // Metallic gray
        "fairy" -> Color(0xFFF48FB1) // Light pink
        else -> Color.Gray // Default for unknown types
    }
}

fun elementIcon(type: String) : Int {
    return when (type.lowercase()) {
        "normal" ->  R.drawable.normal
        "fire" -> R.drawable.fire // Red-orange
        "water" -> R.drawable.water // Deep blue
        "electric" -> R.drawable.electric // Bright yellow
        "grass" -> R.drawable.grass // Fresh green
        "ice" -> R.drawable.water// Cool light blue
        "fighting" -> R.drawable.fighting // Strong red
        "poison" -> R.drawable.ghost // Purple
        "ground" -> R.drawable.fighting // Earthy tan
        "flying" -> R.drawable.normal // Soft light blue
        "psychic" -> R.drawable.ghost // Light pink
        "bug" -> R.drawable.grass // Light green
        "rock" -> R.drawable.fighting // Brown
        "ghost" -> R.drawable.ghost // Dark purple
        "dragon" -> R.drawable.normal// Strong blue
        "dark" -> R.drawable.dark // Deep gray/black
        "steel" -> R.drawable.steel // Metallic gray
        "fairy" -> R.drawable.fairy// Light pink
        else ->R.drawable.normal // Default for unknown types
    }
}

