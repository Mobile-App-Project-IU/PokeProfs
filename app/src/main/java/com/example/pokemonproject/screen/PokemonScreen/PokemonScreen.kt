package com.example.pokemonproject.screen.PokemonScreen

import PokemonDetailScreen
import PokemonListScreen
import android.content.Context
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute

import kotlinx.serialization.Serializable

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun PokemonScreen(
    innerPadding: PaddingValues,
    context: Context,) {
    val navController = rememberNavController()
    SharedTransitionLayout {
        NavHost(navController = navController, startDestination = PokemonListRoute) {
            composable<PokemonListRoute> {
                PokemonListScreen(
                    animatedVisibilityScope = this,
                    onPokemonClick = { id ->
                        navController.navigate(PokemonDetailRout(id))
                    },
                    innerPadding = innerPadding,
                    context = context
                )
            }
            composable<PokemonDetailRout> {
                val args = it.toRoute<PokemonDetailRout>()
                PokemonDetailScreen(
                    animatedVisibilityScope = this,
                    id = args.id,
                )
            }
        }
    }
}



@Serializable
data object PokemonListRoute

@Serializable
data class PokemonDetailRout(val id: Int)