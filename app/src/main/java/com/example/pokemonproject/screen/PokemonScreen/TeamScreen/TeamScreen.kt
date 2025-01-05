package com.example.pokemonproject.screen.PokemonScreen.TeamScreen

import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
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
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pokemonproject.domain.model.Team
import com.example.myapplication.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamScreen(navController: NavController, viewModel: TeamScreenViewModel) {
    // Collect teams from the ViewModel state
    val teams by viewModel.teams.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Pokemon Teams") }) // Top app bar with title
        },
        floatingActionButton = {
            // Floating action button to navigate to the create team screen
            FloatingActionButton(onClick = {
                navController.navigate("create_team_screen")
            }) {
                Text("+") // Text icon to indicate addition
            }
        }
    ) { padding ->
        val context = LocalContext.current // Get the current context for Toasts
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 8.dp) // Added vertical padding for better list spacing
            ) {
                items(teams) { team ->
                    TeamCard(
                        team = team,
                        onDelete = {
                            viewModel.deleteTeam(team) // Delete team action
                            showDeleteToast(context, team) // Show custom toast after deletion
                        }
                    )
                }
            }
        }
    }
}

// Show a custom Toast to notify the user that the team has been deleted
fun showDeleteToast(context: android.content.Context, team: Team) {
    val toastLayout = LayoutInflater.from(context).inflate(R.layout.custom_toast, null)
    val toastIcon: ImageView = toastLayout.findViewById(R.id.toastIcon)
    val toastText: TextView = toastLayout.findViewById(R.id.toastText)

    // Set the icon for the toast to be an information icon
    toastIcon.setImageResource(R.drawable.information_button) // Ensure you have an info icon in your resources

    // Set the text for the toast
    toastText.text = "Team deleted: ${team.name}"

    // Create and show the Toast
    val toast = Toast(context)
    toast.view = toastLayout
    toast.duration = Toast.LENGTH_SHORT
    toast.show()
}

@Composable
fun TeamCard(team: Team, onDelete: () -> Unit) {
    var dragOffset by remember { mutableStateOf(0f) } // Track the drag offset for swipe-to-delete gesture
    var showDeleteIcon by remember { mutableStateOf(false) } // Control whether to show the delete icon
    val context = LocalContext.current // Get the current context for Toasts

    // Handle horizontal drag gestures for swipe-to-delete functionality
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp) // Padding for spacing around each card
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    dragOffset += dragAmount // Update the drag offset as the user drags
                    showDeleteIcon = dragOffset < -150f // Show delete icon when dragged far enough
                }
            }
    ) {
        // Apply an animated offset to the card based on the drag amount
        val animatedOffset by animateFloatAsState(
            targetValue = dragOffset,
            animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy)
        )

        // Card container for each team
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = animatedOffset.dp) // Apply the drag offset to the card's position
                .background(MaterialTheme.colorScheme.surface) // Background color of the card
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp) // Padding inside the card for content
                    .fillMaxWidth()
            ) {
                // Display team name with ellipsis for overflow
                Text(
                    text = team.name,
                    style = MaterialTheme.typography.headlineMedium, // Make the text prominent
                    modifier = Modifier
                        .padding(bottom = 12.dp) // Padding between elements
                        .fillMaxWidth(),
                    maxLines = 1, // Limit to a single line
                    overflow = TextOverflow.Ellipsis // Handle text overflow with ellipsis
                )

                // Display Pokémon names within a row layout
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp) // Space between Pokémon names
                ) {
                    val pokemonNames = team.members.take(6).toMutableList().apply {
                        // Fill remaining slots with "N/A" if there are fewer than 6 Pokémon
                        while (size < 6) {
                            add("N/A")
                        }
                    }

                    // Create boxes for each Pokémon name, clickable for additional info
                    pokemonNames.forEach { pokemonName ->
                        Box(
                            modifier = Modifier
                                .weight(1f) // Distribute space equally
                                .padding(4.dp)
                                .background(MaterialTheme.colorScheme.primaryContainer, shape = MaterialTheme.shapes.medium) // Rounded background
                                .clickable {
                                    // Show the full Pokémon name in a Toast when tapped
                                    Toast.makeText(context, pokemonName, Toast.LENGTH_SHORT).show()
                                }
                        ) {
                            Text(
                                text = pokemonName.replaceFirstChar { it.uppercaseChar() }, // Capitalize the first letter
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.align(Alignment.Center), // Center the text
                                maxLines = 1, // Limit to one line
                                overflow = TextOverflow.Ellipsis // Handle overflow with ellipsis
                            )
                        }
                    }
                }
            }
        }

        // Show delete button when the card is dragged far enough
        if (showDeleteIcon) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd) // Position delete icon at the right side
                    .fillMaxHeight()
                    .width(72.dp)
                    .background(MaterialTheme.colorScheme.error) // Red background for delete action
                    .clickable {
                        onDelete() // Trigger the delete action when clicked
                    }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Team", // Provide accessibility text for the icon
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    tint = Color.White // White icon color for better contrast
                )
            }
        }
    }
}
