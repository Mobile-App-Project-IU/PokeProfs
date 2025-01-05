package com.example.pokemonproject.screen.PokemonScreen.TeamScreen

import android.widget.Toast
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pokemonproject.domain.model.Team


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamScreen(navController: NavController, viewModel: TeamScreenViewModel) {
    val teams by viewModel.teams.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Pokemon Teams") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("create_team_screen")
            }) {
                Text("+")
            }
        }
    ) { padding ->
        val context = LocalContext.current // Get the current context
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn {
                items(teams) { team ->
                    TeamCard(
                        team = team,
                        onDelete = {
                            viewModel.deleteTeam(team)
                            // Use the context to show the Toast
                            Toast.makeText(context, "Team deleted: ${team.name}", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TeamCard(team: Team, onDelete: () -> Unit) {
    var dragOffset by remember { mutableStateOf(0f) } // Track the drag offset
    var showDeleteIcon by remember { mutableStateOf(false) } // Control whether to show the delete icon

    // Handle drag gestures using pointerInput
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    dragOffset += dragAmount // Update the drag offset
                    // Show the delete icon if the card is dragged beyond a threshold (e.g., -150f)
                    showDeleteIcon = dragOffset < -150f
                }
            }
    ) {
        val animatedOffset by animateFloatAsState(
            targetValue = dragOffset,
            animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = animatedOffset.dp) // Apply the drag offset
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Team name
                Text(
                    text = team.name,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp) // Spacing between rows
                )

                // Pokémon names
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val pokemonNames = team.members.take(6).toMutableList().apply {
                        while (size < 6) {
                            add("N/A")
                        }
                    }

                    pokemonNames.forEach { pokemonName ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                        ) {
                            Text(
                                text = pokemonName,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }

        // Show delete button when dragged far enough
        if (showDeleteIcon) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight()
                    .width(72.dp)
                    .background(MaterialTheme.colorScheme.error)
                    .clickable {
                        onDelete() // Trigger the delete action
                    }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Team",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    tint = Color.White
                )
            }
        }
    }
}
