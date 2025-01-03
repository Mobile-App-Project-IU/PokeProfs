import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.pokemonproject.domain.DTO.PokemonDTO
import com.example.pokemonproject.domain.model.PokemonState
import com.example.pokemonproject.domain.model.PokemonStatus
import com.example.pokemonproject.screen.PokemonScreen.PokemonList.PokemonListViewModel
import com.example.pokemonproject.utils.isInternetAvailable
import com.example.myapplication.ui.theme.elementColor
import com.example.pokemonproject.screen.PokemonScreen.PokemonAppBarWithMenu
import com.example.pokemonproject.screen.PokemonScreen.PokemonScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(
    viewModel: PokemonListViewModel = hiltViewModel(),
    innerPadding: PaddingValues,
    context: Context,
    onPokemonClick: (Int) -> Unit,
    isFilterMenuVisible: Boolean, // Add filter visibility state
    onFilterVisibilityChanged: (Boolean) -> Unit // Callback for state updates
) {
    val pokemonState by viewModel.pokemonState.observeAsState(initial = PokemonState())
    var searchQuery by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current // Get the keyboard controller

    val pokemonTypes = listOf(
        "normal", "fire", "water", "electric", "grass", "ice", "fighting", "poison",
        "ground", "flying", "psychic", "bug", "rock", "ghost", "dragon", "dark", "steel", "fairy", "all"
    ).sorted()

    var selectedType by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            PokemonAppBarWithMenu(
                pokemonScreen = PokemonScreen.PokemonList,
                canNavigateBack = false,
                navigateUp = { },
                onMenuClick = { },
                onFilterClick = { onFilterVisibilityChanged(!isFilterMenuVisible) }
            )
        },
        content = {
            Column(modifier = Modifier.padding(it)) {
                // Search bar below the top app bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp),  // Added padding to separate from the app bar
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            viewModel.searchPokemon(it)
                        },
                        placeholder = { Text("Search Pokémon...") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                viewModel.searchPokemon(searchQuery)
                                keyboardController?.hide()
                            }
                        ),
                        colors = TextFieldDefaults.textFieldColors(containerColor = MaterialTheme.colorScheme.surface)
                    )
                }

                AnimatedVisibility(visible = isFilterMenuVisible) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    ) {
                        // Make the filter menu scrollable
                        LazyColumn {
                            items(pokemonTypes) { type ->
                                val typeColor = elementColor(type)
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(typeColor) // Set the background color for each row
                                        .clickable {
                                            if (type == "all") {
                                                // Reset filter if "All" is clicked
                                                viewModel.filterPokemonByType("")
                                                onFilterVisibilityChanged(false) // Close filter menu
                                            } else {
                                                viewModel.filterPokemonByType(type)
                                                onFilterVisibilityChanged(false) // Close filter menu
                                            }
                                        }
                                ) {
                                    Text(
                                        text = type.replaceFirstChar { it.uppercase() }, // Capitalize first letter
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.White, // Make the text white for better contrast
                                        modifier = Modifier
                                            .padding(8.dp) // Add padding inside the row
                                            .fillMaxWidth(), // Fill the width to center the text
                                        textAlign = TextAlign.Center // Center the text
                                    )
                                }
                            }
                        }
                    }
                }


                // Display Pokémon list as before
                when (pokemonState.status) {
                    PokemonStatus.LOADING -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) { CircularProgressIndicator() }
                    }
                    PokemonStatus.ERROR -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) { Text("Error loading Pokémon.", color = MaterialTheme.colorScheme.error) }
                    }
                    PokemonStatus.SUCCESS -> {
                        LazyColumn(
                            contentPadding = PaddingValues(
                                start = 16.dp,
                                end = 16.dp,
                                bottom = innerPadding.calculateBottomPadding() + 10.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(pokemonState.pokemonList) { pokemon ->
                                PokeProfsPokemonCard(
                                    pokemon = pokemon,
                                    context = context,
                                    onPokemonClick = { onPokemonClick(pokemon.id) }
                                )
                            }
                        }
                    }
                    PokemonStatus.INIT -> {}
                }
            }
        }
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("InvalidColorHexValue")
@Composable
fun PokeProfsPokemonCard(
    pokemon: PokemonDTO,
    context: Context,
    onPokemonClick: () -> Unit
) {
    val isConnected = remember { isInternetAvailable(context) }
    val cardBackgroundColor = if (pokemon.types.isNotEmpty()) {
        elementColor(pokemon.types.first()).copy(alpha = 0.5f)
    } else {
        Color(0xFFE0E0E0)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), // Add spacing between cards
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor),
        onClick = onPokemonClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp) // Increase padding inside the card
        ) {
            Box(
                modifier = Modifier
                    .border(
                        width = 2.dp,
                        color = Color(173, 216, 230),
                        shape = RoundedCornerShape(8.dp) // Slightly rounder corners
                    )
                    .size(120.dp) // Increase the size of the image container
                    .padding(8.dp)
            ) {
                AsyncImage(
                    model = pokemon.sprites,
                    contentDescription = pokemon.name,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                if (!isConnected) {
                    Text(
                        text = "No Image",
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp)) // Increase spacing between image and text

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp) // Add right padding
            ) {
                Text(
                    text = pokemon.name.replaceFirstChar { it.uppercase() }, // Capitalize the first letter
                    style = MaterialTheme.typography.titleLarge, // Use a larger font style
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "#${pokemon.id}",
                    style = MaterialTheme.typography.bodyMedium, // Slightly larger font size
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp)) // Add space between text and badges

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp) // Space out badges
                ) {
                    pokemon.types.forEach { type ->
                        Text(
                            text = type.replaceFirstChar { it.uppercase() }, // Capitalize the first letter
                            style = MaterialTheme.typography.labelMedium, // Larger text style for badges
                            color = Color.White,
                            modifier = Modifier
                                .border(1.dp, elementColor(type), RoundedCornerShape(8.dp)) // Add border with rounder corners
                                .background(                                    color = elementColor(type), // Background color based on type
                                    shape = RoundedCornerShape(8.dp) // Rounder corners for badges
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp) // Adjust padding for badge size
                        )
                    }
                }
            }
        }
    }
}

