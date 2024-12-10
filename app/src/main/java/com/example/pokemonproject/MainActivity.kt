package com.example.pokemonproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.pokemonproject.data.Room.AppDatabase
import com.example.pokemonproject.data.Room.Entity.PokemonEntity
import com.example.pokemonproject.screen.PokemonScreen.PokemonScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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


