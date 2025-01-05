package com.example.pokemonproject.screen.PokemonScreen.CreateTeamScreen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import com.example.pokemonproject.screen.PokemonScreen.PokemonAppBarWithMenu
import com.example.pokemonproject.domain.model.Team
import com.example.pokemonproject.screen.PokemonScreen.PokemonScreen

@Composable
fun CreateTeamScreen(
    navController: NavController,
    viewModel: CreateTeamViewModel
) {
    var teamName by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }

    // Track selected Pokémon in each of the 6 slots, default to null
    var selectedPokemon = remember { mutableStateListOf<String?>(null, null, null, null, null, null) }

    val pokemonState by viewModel.pokemonList.observeAsState(emptyList())

    // Handle fetching Pokémon data when screen is launched
    LaunchedEffect(Unit) {
        viewModel.fetchPokemonData()
    }

    // Handle search query updates
    LaunchedEffect(searchQuery) {
        viewModel.searchPokemon(searchQuery)
    }

    // Handle selected Pokémon slot updates from navigation
    val currentBackStackEntry = navController.currentBackStackEntry
    val updatedPokemonName = currentBackStackEntry?.savedStateHandle?.get<String>("selectedPokemonName")
    val slotIndex = currentBackStackEntry?.savedStateHandle?.get<Int>("slotIndex")

    // Restore selected Pokémon state when navigating back
    LaunchedEffect(updatedPokemonName, slotIndex) {
        if (updatedPokemonName != null && slotIndex != null) {
            selectedPokemon[slotIndex] = updatedPokemonName
            // Clear the saved state after updating
            currentBackStackEntry.savedStateHandle.remove<String>("selectedPokemonName")
            currentBackStackEntry.savedStateHandle.remove<Int>("slotIndex")
        }
    }

    val context = LocalContext.current
    val showToast = remember { mutableStateOf(false) }

    // Scaffold layout with Material3 AppBar
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
            // Main content of the screen wrapped with vertical scroll
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(top = 16.dp)
                    .verticalScroll(rememberScrollState()) // Ensuring everything is scrollable
            ) {
                // Team Name Input Field
                TextField(
                    value = teamName,
                    onValueChange = { teamName = it },
                    label = { Text("Team Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Search Pokémon Input Field
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search Pokémon") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Dynamically display search results
                if (searchQuery.isNotEmpty()) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        pokemonState.forEach { pokemonDTO ->
                            Text(
                                text = pokemonDTO.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .clickable {
                                        // Add Pokémon to the first available slot if there is space
                                        val firstAvailableIndex = selectedPokemon.indexOfFirst { it == null }
                                        if (firstAvailableIndex != -1) {
                                            selectedPokemon[firstAvailableIndex] = pokemonDTO.name
                                            searchQuery = "" // Clear the search bar after selection
                                        } else {
                                            Toast.makeText(context, "Already Enough 6 Pokemons", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Display selected Pokémon in each slot
                selectedPokemon.forEachIndexed { index, pokemonName ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween, // Align items at both ends
                        verticalAlignment = Alignment.CenterVertically // Center the items vertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Slot ${index + 1}", modifier = Modifier.padding(bottom = 8.dp))
                            Text(
                                text = pokemonName ?: "Choose a Pokémon",
                                modifier = Modifier
                                    .padding(8.dp)
                                    .clickable {
                                        // Save the current state (slot index and pokemon name) before navigating
                                        navController.previousBackStackEntry?.savedStateHandle?.set("selectedPokemonName", pokemonName)
                                        navController.previousBackStackEntry?.savedStateHandle?.set("slotIndex", index)

                                        val pokemonId = viewModel.getPokemonIdByName(pokemonName)
                                        navController.navigate("pokemon_detail_route/$pokemonId")
                                    }
                            )
                        }

                        // Trash bin icon aligned to the right
                        if (pokemonName != null) {
                            IconButton(
                                onClick = {
                                    selectedPokemon[index] = null // Clear the selected Pokémon from this slot
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Pokémon",
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }
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
                            viewModel.saveTeam(newTeam)

                            // Show Toast message
                            showToast.value = true
                            navController.popBackStack() // Navigate back after saving the team
                        }
                    },
                    enabled = teamName.isNotBlank() && selectedPokemon.any { it != null }
                ) {
                    Text("Save Team")
                }

                // Show Toast message after saving the team
                if (showToast.value) {
                    LaunchedEffect(Unit) {
                        Toast.makeText(context, "Team Saved", Toast.LENGTH_SHORT).show()
                        showToast.value = false // Reset the state after showing the toast
                    }
                }
            }
        }
    )
}
