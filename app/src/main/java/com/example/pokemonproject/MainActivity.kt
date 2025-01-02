package com.example.pokemonproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.pokemonproject.screen.PokemonScreen.PokemonScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge UI for immersive experience
        enableEdgeToEdge()

        // Set up the content of the activity using Jetpack Compose
        setContent {
            MyApplicationTheme {
                // Pass the current context to PokemonScreen composable
                PokemonScreen(context = this)
            }
        }

        // Future database setup (Room database) - Uncomment to enable functionality
        // setupDatabase()
    }

    // This method will handle Room database setup if needed in the future
    private fun setupDatabase() {
        // Code for setting up Room database goes here
        // val db = Room.databaseBuilder(
        //     this,
        //     AppDatabase::class.java,
        //     "pokemon_database"
        // ).build()
        // val pokemonDao = db.pokemonDao()
        // lifecycleScope.launch {
        //     pokemonDao.insert(PokemonEntity(0, "Pikachu"))
        //     val pokemonList = pokemonDao.getAll().collect { pokemonList ->
        //         pokemonList.forEach { pokemon ->
        //             println("Pokemon: ${pokemon.name}")
        //         }
        //     }
        // }
    }
}
