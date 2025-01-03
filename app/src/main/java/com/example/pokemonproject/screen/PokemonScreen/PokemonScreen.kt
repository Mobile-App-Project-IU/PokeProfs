@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pokemonproject.screen.PokemonScreen

import PokemonDetailScreen
import PokemonListScreen
import android.content.Context
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalDensity

enum class PokemonScreen(val title: String) {
    PokemonDetail("Pokemon Detail"),
    PokemonList("Pokemon List")
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun PokemonScreen(context: Context) {
    val navController = rememberNavController()
    var screen by remember { mutableStateOf(PokemonScreen.PokemonList) }
    var canNavigateBack by remember { mutableStateOf(false) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    // State for filter visibility
    var isFilterMenuVisible by remember { mutableStateOf(false) }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val screenWidth = with(LocalDensity.current) { constraints.maxWidth.toDp() } // Convert to Dp

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                if (drawerState.isOpen) {
                    Surface(
                        modifier = Modifier.width(screenWidth * 0.5f),
                        color = MaterialTheme.colorScheme.surface,
                        contentColor = contentColorFor(MaterialTheme.colorScheme.surface)
                    ) {
                        DrawerContent(onOptionSelected = { option ->
                            coroutineScope.launch { drawerState.close() }
                        })
                    }
                }
            }
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    PokemonAppBarWithMenu(
                        pokemonScreen = screen,
                        canNavigateBack = canNavigateBack,
                        navigateUp = {
                            navController.popBackStack()
                            canNavigateBack = false
                            screen = PokemonScreen.PokemonList
                        },
                        onMenuClick = { coroutineScope.launch { drawerState.open() } },
                        onFilterClick = { isFilterMenuVisible = !isFilterMenuVisible } // Update filter visibility state
                    )
                }
            ) { innerPadding ->
                SharedTransitionLayout {
                    NavHost(navController = navController, startDestination = PokemonListRoute) {
                        composable<PokemonListRoute> {
                            PokemonListScreen(
                                onPokemonClick = { id ->
                                    navController.navigate(PokemonDetailRout(id))
                                    screen = PokemonScreen.PokemonDetail
                                    canNavigateBack = true
                                },
                                innerPadding = innerPadding,
                                context = context,
                                isFilterMenuVisible = isFilterMenuVisible, // Pass the filter state
                                onFilterVisibilityChanged = { isFilterMenuVisible = it } // Allow child to update state
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
    }
}

@Composable
fun PokemonAppBarWithMenu(
    pokemonScreen: PokemonScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    onMenuClick: () -> Unit,
    onFilterClick: () -> Unit, // Add onFilterClick here
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
                        contentDescription = "Back"
                    )
                }
            } else {
                IconButton(onClick = onMenuClick) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Menu"
                    )
                }
            }
        },
        actions = {
            // Only show filter button on PokemonList screen
            if (pokemonScreen == PokemonScreen.PokemonList) {
                IconButton(onClick = onFilterClick) {
                    Icon(
                        imageVector = Icons.Filled.FilterList,
                        contentDescription = "Filter"
                    )
                }
            }
        }
    )
}

@Composable
fun DrawerContent(onOptionSelected: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Header Section with app name
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "PokeProfs",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
//                // Optionally, add a profile picture here
//                Icon(Icons.Filled.Menu, contentDescription = "Profile", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }

        Divider()

        // Menu options
        val options = listOf("Pokedex", "Pokemon Team", "Help & Feedback", "About Us")
        options.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().clickable { onOptionSelected(option) }
            ) {
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = option,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }
            Divider()
        }

        // Footer Section (Optional)
        Spacer(modifier = Modifier.weight(1f)) // To push footer to the bottom
        Divider()
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
//            TextButton(onClick = { onOptionSelected("Settings") }) {
//                Text("Settings")
//            }
        }
    }
}

@Serializable
data object PokemonListRoute

@Serializable
data class PokemonDetailRout(val id: Int)
