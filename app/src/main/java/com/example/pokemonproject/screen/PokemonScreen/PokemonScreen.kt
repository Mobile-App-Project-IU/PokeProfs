@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pokemonproject.screen.PokemonScreen

import PokemonDetailScreen
import PokemonListScreen
import android.content.Context
import androidx.compose.animation.ExperimentalSharedTransitionApi
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalDensity
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pokemonproject.data.Room.PokemonDatabase
import com.example.pokemonproject.domain.repository.TeamRepository
import com.example.pokemonproject.screen.PokemonScreen.CreateTeamScreen.CreateTeamScreen
import com.example.pokemonproject.screen.PokemonScreen.CreateTeamScreen.CreateTeamViewModel
import com.example.pokemonproject.screen.PokemonScreen.TeamScreen.TeamScreen
import com.example.pokemonproject.screen.PokemonScreen.TeamScreen.TeamScreenViewModel

enum class PokemonScreen(val title: String) {
    PokemonDetail("Pokemon Detail"),
    PokemonList("Pokemon List"),
    TeamScreen("Pokemon Team"),
    CreateTeamScreen("Create Team")
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

    // Create TeamRepository instance from the database
    val teamDatabase = PokemonDatabase.getDatabase(context) // Get the instance of PokemonDatabase
    val teamRepository = TeamRepository(teamDao = teamDatabase.teamDao()) // Use teamDao() from the database

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val screenWidth = with(LocalDensity.current) { constraints.maxWidth.toDp() }

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
                            when (option) {
                                "Pokemon Team" -> navController.navigate("TeamScreen")
                                "Pokedex" -> navController.navigate("${PokemonListRoute.route}/{slotIndex}/{isForTeamBuilder}")
                            }
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
                        onFilterClick = { isFilterMenuVisible = !isFilterMenuVisible },
                    )
                }
            ) { innerPadding ->
                NavHost(navController = navController, startDestination = "${PokemonListRoute.route}/{slotIndex}/{isForTeamBuilder}") {
                    composable("${PokemonListRoute.route}/{slotIndex}/{isForTeamBuilder}") { backStackEntry ->
                        // Extract slotIndex and isForTeamBuilder arguments from the back stack
                        val slotIndex =
                            backStackEntry.arguments?.getString("slotIndex")?.toIntOrNull() ?: -1
                        val isForTeamBuilder =
                            backStackEntry.arguments?.getString("isForTeamBuilder")?.toBoolean()
                                ?: false
                        canNavigateBack = false
                        // Pass the onPokemonTeamSelect lambda here
                        PokemonListScreen(
                            innerPadding = innerPadding,
                            context = context,
                            onPokemonClick = { pokemonId ->
                                navController.navigate(PokemonDetailRoute(pokemonId)) // Navigate to Pokémon detail
                            },
                            onPokemonTeamSelect = { selectedPokemon ->
                                // Handle Pokémon team selection
                                navController.previousBackStackEntry?.savedStateHandle?.set(
                                    "selectedPokemon",
                                    selectedPokemon
                                )
                                navController.previousBackStackEntry?.savedStateHandle?.set(
                                    "slotIndex",
                                    slotIndex
                                )
                                navController.popBackStack() // Return to the previous screen
                            },
                            isFilterMenuVisible = isFilterMenuVisible,
                            onFilterVisibilityChanged = { isFilterMenuVisible = it },
                            slotIndex = slotIndex,
                            isForTeamBuilder = isForTeamBuilder
                        )
                    }


                        composable<PokemonDetailRoute> {
                        val args = it.toRoute<PokemonDetailRoute>()
                        screen = PokemonScreen.PokemonDetail
                        canNavigateBack = true
                        PokemonDetailScreen(
                            animatedVisibilityScope = this,
                            id = args.id,
                        )
                    }

                    composable("TeamScreen") {
                        // Pass the teamRepository to the viewModel
                        canNavigateBack = false
                        screen = PokemonScreen.TeamScreen
                        TeamScreen(navController = navController, viewModel = TeamScreenViewModel(repository = teamRepository))
                    }

                    composable("create_team_screen") {
                        canNavigateBack = true
                        val createTeamViewModel: CreateTeamViewModel = hiltViewModel() // Automatically inject the ViewModel

                        CreateTeamScreen(
                            navController = navController,
                            createTeamViewModel = createTeamViewModel
                        )
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
    onFilterClick: () -> Unit,
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
            when (pokemonScreen) {
                PokemonScreen.PokemonList -> {
                    IconButton(onClick = onFilterClick) {
                        Icon(
                            imageVector = Icons.Filled.FilterList,
                            contentDescription = "Filter"
                        )
                    }
                }
                else -> {}
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
            }
        }

        Divider()

        // Menu options
        val options = listOf("Pokedex", "Pokemon Team", "Help & Feedback", "About Us")
        options.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOptionSelected(option) }
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
        Spacer(modifier = Modifier.weight(1f))
    }
}


@Serializable
data object PokemonListRoute{
    const val route = "pokemon_list_route"
}

@Serializable
data class PokemonDetailRoute(val id: Int)