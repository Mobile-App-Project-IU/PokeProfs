package com.example.pokemonproject.screen.PokemonScreen

import PokemonDetailScreen
import PokemonListScreen
import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.myapplication.R

import kotlinx.serialization.Serializable
enum class pokemonScreen(val title: String) {
    PokeMonDetail("PokeMonDetail"),
    PokeMonList("PokeMonList")
}
@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PokemonAppBar(
    pokemonScreen:pokemonScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(pokemonScreen.title) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "back"
                    )
                }
            }
        }
    )
}
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun PokemonScreen(context: Context,) {
    val navController = rememberNavController()
    var screen by remember { mutableStateOf(pokemonScreen.PokeMonList) }
    var navigate by remember { mutableStateOf(false) }
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        PokemonAppBar(
            pokemonScreen = screen,
            canNavigateBack = navigate,
            navigateUp = { navController.popBackStack();
                navigate = false;
                screen=pokemonScreen.PokeMonList
            }
        )
    }) { innerPadding ->
        SharedTransitionLayout {
            NavHost(navController = navController, startDestination = PokemonListRoute) {
                composable<PokemonListRoute> {
                    PokemonListScreen(
                        onPokemonClick = { id ->
                            navController.navigate(PokemonDetailRout(id))
                            screen = pokemonScreen.PokeMonDetail
                            navigate = true
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

}



@Serializable
data object PokemonListRoute

@Serializable
data class PokemonDetailRout(val id: Int)