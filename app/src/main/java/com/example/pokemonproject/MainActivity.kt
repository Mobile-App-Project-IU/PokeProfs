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
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                PokemonScreen(
                    context = this@MainActivity
                )
            }
        }
//        val db=Room.databaseBuilder(
//            this@MainActivity,
//            AppDatabase::class.java,
//            "test_database"
//        ).build()
//        val pokemonDao = db.pokemonDao()
//        lifecycleScope.launch {
//            pokemonDao.insert(PokemonEntity(0, "Lemanh@1412"));
//            val pokemon = pokemonDao.getAll().collect { pokemonList ->
//                // Access the List<PokemonEntity> here
//                pokemonList.forEach { pokemon ->
//                    println("Test Database: ${pokemon.name}")
//                }
//
//        }
//
//    }
    }
}


