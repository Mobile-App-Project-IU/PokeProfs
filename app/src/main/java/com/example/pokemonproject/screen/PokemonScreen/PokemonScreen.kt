@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pokemonproject.screen.PokemonScreen

import PokemonDetailScreen
import PokemonListScreen
import android.content.Context
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.R
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.pokemonproject.data.Room.PokemonDatabase
import com.example.pokemonproject.domain.repository.TeamRepository
import com.example.pokemonproject.screen.PokemonScreen.AboutUsScreen.AboutUsScreen
import com.example.pokemonproject.screen.PokemonScreen.HelpSuportScreen.HelpSupportScreen
import com.example.pokemonproject.screen.PokemonScreen.CreateTeamScreen.CreateTeamScreen
import com.example.pokemonproject.screen.PokemonScreen.CreateTeamScreen.CreateTeamViewModel
import com.example.pokemonproject.screen.PokemonScreen.TeamScreen.TeamScreen
import com.example.pokemonproject.screen.PokemonScreen.TeamScreen.TeamScreenViewModel

enum class PokemonScreen(val title: String) {
    PokemonDetail("Pokemon Detail"),
    PokemonList("Pokemon List"),
    TeamScreen("Pokemon Team"),
    CreateTeamScreen("Create Team"),
    HelpSupportScreen("Help & Support"),
    AboutUSScreen("About Us")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonScreen(context: Context) {
    MyApplicationTheme {
        val navController = rememberNavController()
        var screen by remember { mutableStateOf(PokemonScreen.PokemonList) }
        var canNavigateBack by remember { mutableStateOf(false) }
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val coroutineScope = rememberCoroutineScope()

        // State for filter visibility
        var isFilterMenuVisible by remember { mutableStateOf(false) }

        // Create TeamRepository instance from the database
        val teamDatabase = PokemonDatabase.getDatabase(context)
        val teamRepository = TeamRepository(teamDao = teamDatabase.teamDao())

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val screenWidth = with(LocalDensity.current) { constraints.maxWidth.toDp() }
            val screenHeight = with(LocalDensity.current) { constraints.maxHeight.toDp() }

            // Use the ModalDrawer for smaller screens (phones)
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    if (drawerState.isOpen) {
                        Surface(
                            modifier = Modifier.width(if (screenWidth < screenHeight) screenWidth * 0.75f else screenWidth * 0.4f),
                            color = MaterialTheme.colorScheme.surface,
                            contentColor = contentColorFor(MaterialTheme.colorScheme.surface)
                        ) {
                            DrawerContent(onOptionSelected = { option ->
                                coroutineScope.launch { drawerState.close() }
                                when (option) {
                                    "Pokemon Team" -> navController.navigate("TeamScreen")
                                    "Pokedex" -> navController.navigate(PokemonListRoute.ROUTE)
                                    "Help & Feedback" -> navController.navigate("help_support_screen")
                                    "About Us" -> navController.navigate("about_us_screen")
                                    else -> {}
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
                    val padding = if (screenHeight < screenWidth) 16.dp else 8.dp

                    NavHost(
                        navController = navController,
                        startDestination = PokemonListRoute.ROUTE
                    ) {
                        composable(PokemonListRoute.ROUTE) {
                            canNavigateBack = false
                            screen = PokemonScreen.PokemonList
                            PokemonListScreen(
                                innerPadding = innerPadding,
                                context = context,
                                onPokemonClick = { pokemonId ->
                                    navController.navigate("pokemon_detail_route/$pokemonId")
                                },
                                isFilterMenuVisible = isFilterMenuVisible,
                                onFilterVisibilityChanged = { isFilterMenuVisible = it },
                            )
                        }

                        composable("pokemon_detail_route/{id}") { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
                            screen = PokemonScreen.PokemonDetail
                            canNavigateBack = true
                            PokemonDetailScreen(
                                id = id,
                                animatedVisibilityScope = this
                            )
                        }

                        composable("TeamScreen") {
                            canNavigateBack = false
                            screen = PokemonScreen.TeamScreen
                            TeamScreen(
                                navController = navController,
                                viewModel = TeamScreenViewModel(repository = teamRepository)
                            )
                        }

                        composable("create_team_screen") {
                            canNavigateBack = true
                            screen = PokemonScreen.CreateTeamScreen
                            val createTeamViewModel: CreateTeamViewModel = hiltViewModel()
                            CreateTeamScreen(
                                navController = navController,
                                viewModel = createTeamViewModel
                            )
                        }

                        composable("help_support_screen") {
                            canNavigateBack = false
                            screen = PokemonScreen.HelpSupportScreen
                            HelpSupportScreen()
                        }

                        composable("about_us_screen") {
                            canNavigateBack = false
                            screen = PokemonScreen.AboutUSScreen
                            AboutUsScreen()
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
    onFilterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(pokemonScreen.title, style = MaterialTheme.typography.headlineMedium) },
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
        // Header Section with app name and logo
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Logo for the App
                Image(
                    painter = painterResource(id = R.drawable.pokedex_logo), // Replace with your logo file
                    contentDescription = "Pokedex Logo",
                    modifier = Modifier
                        .size(150.dp) // Make the logo bigger
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 16.dp)
                )
                // App Name and Tagline
                Text(
                    text = "PokeProfs",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = 30.sp,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Let's catch them all! We are Pokemon Professionals",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 16.sp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        Divider()

        // Menu options with icons
        val options = listOf(
            Pair("Pokedex", Icons.Filled.List),
            Pair("Pokemon Team", Icons.Filled.Group),
            Pair("Help & Feedback", Icons.Filled.Help),
            Pair("About Us", Icons.Filled.Info)
        )

        options.forEach { (label, icon) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOptionSelected(label) }
                    .padding(12.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    modifier = Modifier.padding(end = 16.dp)
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 18.sp
                    ),
                    color = MaterialTheme.colorScheme.onBackground
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
    const val ROUTE = "pokemon_list_route"
}
