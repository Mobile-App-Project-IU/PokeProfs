package com.example.pokemonproject.screen.PokemonScreen.CreateTeamScreen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalContext
import com.example.pokemonproject.screen.PokemonScreen.PokemonAppBarWithMenu
import com.example.pokemonproject.screen.PokemonScreen.PokemonScreen
import com.example.pokemonproject.domain.model.Team

@Composable
fun CreateTeamScreen(
    navController: NavController,
    createTeamViewModel: CreateTeamViewModel
) {
    var teamName by remember { mutableStateOf("") }

    // Initialize the selected Pokémon slots (with default Pokémon or nulls)
    var selectedPokemon = remember { mutableStateListOf<String?>(null, null, null, null, null, null) }

    // Search query state
    var searchQuery by remember { mutableStateOf("") }
    val pokemonState by createTeamViewModel.pokemonList.observeAsState(emptyList())

    // Fetch Pokémon data when screen is launched
    LaunchedEffect(Unit) {
        createTeamViewModel.fetchPokemonData()
    }

    // Trigger search when the query changes
    LaunchedEffect(searchQuery) {
        createTeamViewModel.searchPokemon(searchQuery)
    }

    // Handle selected Pokémon from navigation
    val currentBackStackEntry = navController.currentBackStackEntry
    val updatedPokemonName = currentBackStackEntry?.savedStateHandle?.get<String>("selectedPokemonName")
    val slotIndex = currentBackStackEntry?.savedStateHandle?.get<Int>("slotIndex")

    // Update the selected Pokémon slot only when the update is needed
    LaunchedEffect(updatedPokemonName, slotIndex) {
        if (updatedPokemonName != null && slotIndex != null) {
            selectedPokemon[slotIndex] = updatedPokemonName
            currentBackStackEntry.savedStateHandle.remove<String>("selectedPokemonName")
            currentBackStackEntry.savedStateHandle.remove<Int>("slotIndex")
        }
    }

    // Display a Toast message and navigate back after saving the team
    val context = LocalContext.current
    val showToast = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            PokemonAppBarWithMenu(
                pokemonScreen = PokemonScreen.CreateTeamScreen,
                canNavigateBack = true,
                navigateUp = { navController.popBackStack() },
                onMenuClick = { /* handle menu click */ },
                onFilterClick = { /* handle filter click */ }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(top = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Team Name TextField
                TextField(
                    value = teamName,
                    onValueChange = { teamName = it },
                    label = { Text("Team Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Search Pokémon TextField
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search Pokémon") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Display filtered Pokémon list as a dropdown
                if (searchQuery.isNotEmpty()) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        pokemonState.forEach { pokemonDTO ->
                            Text(
                                text = pokemonDTO.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .clickable {
                                        // Add Pokémon to the first available slot
                                        val firstAvailableIndex = selectedPokemon.indexOfFirst { it == null }
                                        if (firstAvailableIndex != -1) {
                                            selectedPokemon[firstAvailableIndex] = pokemonDTO.name
                                        }
                                    }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Selected Pokémon Text Views (Slots)
                selectedPokemon.forEachIndexed { index, pokemonName ->
                    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                        Text("Slot ${index + 1}", modifier = Modifier.padding(bottom = 8.dp))
                        Text(
                            text = pokemonName ?: "Choose a Pokémon",
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable {
                                    // Navigate to select a Pokémon for this slot
                                    navController.navigate("pokemon_list_route/$index")
                                }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Choose Pokémon Button
                Button(
                    onClick = {
                        // Convert selected Pokémon to a comma-separated string
                        val selectedPokemonNames = selectedPokemon.filterNotNull().joinToString(",")
                        navController.navigate("pokemon_list_route/$teamName/$selectedPokemonNames")
                    },
                    enabled = teamName.isNotBlank()
                ) {
                    Text("Choose Pokémon")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Save Team Button
                Button(
                    onClick = {
                        if (teamName.isNotBlank()) {
                            val newTeam = Team(
                                name = teamName,
                                members = selectedPokemon.filterNotNull() // Filter out nulls
                            )
                            createTeamViewModel.saveTeam(newTeam)

                            // Show Toast and navigate back after saving
                            showToast.value = true
                            // Navigate back to TeamScreen after saving
                            navController.popBackStack()
                        }
                    },
                    enabled = teamName.isNotBlank()
                ) {
                    Text("Save Team")
                }

                // Show Toast message after saving the team
                if (showToast.value) {
                    LaunchedEffect(Unit) {
                        Toast.makeText(context, "Save Complete", Toast.LENGTH_SHORT).show()
                        showToast.value = false // Reset the state after showing the toast
                    }
                }
            }
        }
    )
}
