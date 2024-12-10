import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.myapplication.ui.theme.elementColor
import com.example.pokemonproject.data.network.DTO.PokemonDTO
import com.example.pokemonproject.domain.model.PokemonState
import com.example.pokemonproject.domain.model.PokemonStatus
import com.example.pokemonproject.screen.PokemonScreen.PokemonDetailScreen.PokemonScreenViewModel


@Composable
fun PokemonDetailScreen(
    viewModel: PokemonScreenViewModel = hiltViewModel(),
    id: Int,
    animatedVisibilityScope: AnimatedVisibilityScope,
    pokemonImageSize : Dp = 200.dp
) {
    LaunchedEffect(key1 = true) {
        viewModel.fetchPokemon(id)
    }
    val pokemonState by viewModel.pokemonState.observeAsState(initial = PokemonState())
    val pokemon  = pokemonState.pokemon ?: PokemonDTO(types = listOf());

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Black, // Starting color
                            pokemon.types.firstOrNull()?.let {
                                elementColor(it).copy(alpha = 0.7f)
                            } ?: Color.Gray.copy(alpha = 0.5f) // Fallback color if the first type is null
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        )
        {
            when (pokemonState.status) {
                PokemonStatus.LOADING -> {
                    CircularProgressIndicator()
                }
                PokemonStatus.ERROR -> {
                    Text(
                        text = "No internet Connection",
                        style = TextStyle(
                            color = Color.Red,
                            fontSize = 20.sp
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                PokemonStatus.SUCCESS -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        // Display the name above the image
                        Text(
                            text = pokemon.name,
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 20.sp
                            ),
                            modifier = Modifier.padding(bottom = 8.dp)

                        )
                        Text(
                            text = pokemon.description ?:" null",
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 20.sp
                            ),
                            modifier = Modifier.padding(bottom = 8.dp)

                        )

                        // Display the image using Coil's AsyncImage
                        AsyncImage(
                            model = pokemon.sprites,
                            contentDescription = "Pokemon Image",
                            modifier = Modifier
                                .padding(bottom = 5.dp)
                                .size(pokemonImageSize)
                        )
                        Text(
                            text = pokemon.name.replaceFirstChar { it.uppercaseChar() },
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                            ),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        PokemonTypesRow(types = listOf("Normal", "Flying"))
                    }
                }
                PokemonStatus.INIT -> TODO()
            }

        }
        // Display the name above the image
    }
}

@Composable
fun PokemonTypesRow(types: List<String>) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp), // Space between items
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(16.dp) // Padding around the Row
    ) {
        types.forEach { type ->
            Text(
                text = type.replaceFirstChar { it.uppercaseChar() }, // Capitalize first letter
                style = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp
                ),
                modifier = Modifier
                    .background(
                        color = Color.Gray , // Dynamically set background color
                        shape = RoundedCornerShape(50) // Rounded pill shape
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp) // Inner padding
            )
        }
    }
}