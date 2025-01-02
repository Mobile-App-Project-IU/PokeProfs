import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(
    viewModel: PokemonListViewModel = hiltViewModel(),
    innerPadding: PaddingValues,
    context: Context,
    onPokemonClick: (Int) -> Unit,
) {
    val pokemonState by viewModel.pokemonState.observeAsState(initial = PokemonState())
    var searchQuery by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current // Get the keyboard controller

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "PokeProfs",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    viewModel.searchPokemon(it) // Trigger search on text change
                },
                placeholder = { Text("Search Pokémon...") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        viewModel.searchPokemon(searchQuery)
                        keyboardController?.hide() // Hide the keyboard on search
                    }
                ),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    viewModel.searchPokemon(searchQuery)
                    keyboardController?.hide() // Hide the keyboard when Search is clicked
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Search")
            }
        }

        when (pokemonState.status) {
            PokemonStatus.LOADING -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            PokemonStatus.ERROR -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error loading Pokémon.", color = MaterialTheme.colorScheme.error)
                }
            }
            PokemonStatus.SUCCESS -> {
                val pokemonList = pokemonState.pokemonList
                LazyColumn(
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = innerPadding.calculateBottomPadding() + 10.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Text(
                            text = "Pokédex",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    items(pokemonList) { pokemon ->
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
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor),
        onClick = onPokemonClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .border(
                        width = 2.dp,
                        color = Color(173, 216, 230),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .size(100.dp)
                    .padding(10.dp)
            ) {
                AsyncImage(
                    model = pokemon.sprites,
                    contentDescription = pokemon.name,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(4.dp)),
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

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = pokemon.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "#${pokemon.id}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.align(Alignment.Bottom)
            ) {
                Row {
                    pokemon.types.forEach { type ->
                        Text(
                            text = type,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            modifier = Modifier
                                .background(
                                    color = elementColor(type),
                                    shape = RoundedCornerShape(5.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                .clip(RoundedCornerShape(4.dp))
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            }
        }
    }
}
