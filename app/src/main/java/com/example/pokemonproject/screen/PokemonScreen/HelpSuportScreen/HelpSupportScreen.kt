package com.example.pokemonproject.screen.PokemonScreen.HelpSuportScreen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pokemonproject.screen.PokemonScreen.PokemonAppBarWithMenu
import com.example.pokemonproject.screen.PokemonScreen.PokemonScreen

@Composable
fun HelpSupportScreen(
    canNavigateBack: Boolean = false,
    navigateUp: () -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            PokemonAppBarWithMenu(
                pokemonScreen = PokemonScreen.HelpSupportScreen, // Reusing the TopAppBar from PokemonScreen
                canNavigateBack = canNavigateBack,
                navigateUp = navigateUp,
                onMenuClick = onMenuClick,
                onFilterClick = { /* Handle filter click if needed */ }
            )
        },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                // Coming Soon Text
                Text(
                    text = "Coming Soon!",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Additional Placeholder Text
                Text(
                    text = "This feature is under development. Please check back later!",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    )
}
