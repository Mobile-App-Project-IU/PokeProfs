
import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.pokemonproject.data.network.DTO.PokemonDTO
import com.example.pokemonproject.domain.model.PokemonState
import com.example.pokemonproject.domain.model.PokemonStatus
import com.example.pokemonproject.screen.PokemonScreen.PokemonList.PokemonListViewModel
import com.example.pokemonproject.utils.isInternetAvailable
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.example.myapplication.ui.theme.elementColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(
    viewModel: PokemonListViewModel = hiltViewModel(),
    innerPadding: PaddingValues,
    context: Context,
    animatedVisibilityScope: AnimatedContentScope,
    onPokemonClick: (Int) -> Unit,
) {
    val pokemonState by viewModel.pokemonState.observeAsState(initial = PokemonState())
    var searchQuery by remember { mutableStateOf("") }
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Pokemons",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Search") },
                singleLine = true, // Make it a single-line TextField-
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search // Set IME action to "Search"
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        viewModel.searchPokemon(searchQuery)
                    }
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                viewModel.searchPokemon(searchQuery)
            }) {
                Text("Search")
            }
        }
        when(pokemonState.status){
            PokemonStatus.LOADING -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            PokemonStatus.ERROR -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error")
                }
            }
            PokemonStatus.SUCCESS -> {
                val pokemonList = pokemonState.pokemonList
                LazyColumn(
                    contentPadding = PaddingValues(
                        top = innerPadding.calculateTopPadding() + 10.dp,
                        start = 20.dp,
                        end = 20.dp,
                        bottom = innerPadding.calculateBottomPadding() + 10.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Text(
                            text = "Pokedex",
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Bold
                        )

                    }
                    items(pokemonList) { p ->
                        PokemonCard(
                            pokemon = p,context= context,onPokemonClick = {onPokemonClick(p.id)}
                        )
                    }
                }

            }
            PokemonStatus.INIT -> TODO()

        }

    }



    }





@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("InvalidColorHexValue")
@Composable
fun PokemonCard(pokemon: PokemonDTO, context: Context,onPokemonClick: () -> Unit) {
    val isConnected = remember { isInternetAvailable(context) }
    val cardBackgroundColor = if (pokemon.types.isNotEmpty()) {
        elementColor(pokemon.types.first()).copy(alpha = 0.5f) // 50% opacity
    } else {
        Color(0xFFE0E0E0)
    }
    Card(
        modifier = Modifier

            .fillMaxWidth(),
        colors = androidx.compose.material3.CardDefaults.cardColors( // Use CardDefaults to set the background color
            containerColor = cardBackgroundColor
        ),
        onClick = onPokemonClick

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            // Box for the image
            Box(
                modifier = Modifier
                    .border(
                        width = 2.dp,
                        color = Color(173, 216, 230), // Light blue color
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(10.dp) // Adjusted padding for image fitting
            ) {
                if(!isConnected){
                    Text(text ="No image")
                }else{
                    AsyncImage(
                        model = pokemon.sprites,
                        contentDescription = pokemon.name,
                        modifier = Modifier
                            .height(40.dp)
                            .width(40.dp) // Adjusted size for image fitting
                            .clip(RoundedCornerShape(4.dp)),
                        contentScale = ContentScale.Crop
                    )


                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Name and number of the Pokemon
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = pokemon.name,
                    maxLines = 1, // Restrict to one line
                    overflow = TextOverflow.Ellipsis, // Show "..." if the text overflows
                    modifier = Modifier.fillMaxWidth() // Ensure the text expands to available width
                )
                Text(
                    text = "#${pokemon.id}",
                    color = Color.Gray
                )
            }

            // Pokemon types
            Column(
                modifier = Modifier.align(Alignment.Bottom),
                horizontalAlignment = Alignment.End
            ) {
                Row {
                    pokemon.types.forEach { type ->
                        Text(
                            text = type,
                            color = Color.White,
                            modifier = Modifier
                                .background(
                                    color = elementColor(type),
                                    shape = RoundedCornerShape(5.dp) // Adjust corner radius as needed
                                )
                                .padding(
                                    horizontal = 6.dp,
                                    vertical = 2.dp
                                ) // Adjust padding for type label
                                .clip(RoundedCornerShape(4.dp))
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            }
        }    }
}

