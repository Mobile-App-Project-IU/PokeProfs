import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.pokemonproject.domain.model.PokemonState
import com.example.pokemonproject.domain.model.PokemonStatus
import com.example.pokemonproject.screen.PokemonScreen.PokemonDetailScreen.PokemonScreenViewModel


@Composable
fun PokemonDetailScreen(
    viewModel: PokemonScreenViewModel = hiltViewModel(),
    id: Int,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    LaunchedEffect(key1 = true) {
        viewModel.fetchPokemon(id)
    }
    val pokemonState by viewModel.pokemonState.observeAsState(initial = PokemonState())
    Column(

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
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
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Display the name above the image
                        Text(
                            text = pokemonState.pokemon?.name ?: "",
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 20.sp
                            ),
                            modifier = Modifier.padding(bottom = 8.dp)

                        )
                        // Display the image using Coil's AsyncImage
                        AsyncImage(
                            model = pokemonState.pokemon?.sprites ?: "",
                            contentDescription = "Pokemon Image",
                            modifier = Modifier
                                .padding(8.dp)
                        )
                    }
                }

                PokemonStatus.INIT -> TODO()
            }

        }
        // Display the name above the image
    }
}