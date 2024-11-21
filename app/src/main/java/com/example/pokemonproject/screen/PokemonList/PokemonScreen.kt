import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.pokemonproject.data.network.DTO.PokemonDTO
import com.example.pokemonproject.screen.PokemonList.PokemonViewModel


@Composable
fun PokemonScreen(
    viewModel: PokemonViewModel ,
    innerPadding: PaddingValues
) {
    val pokemonList by viewModel.pokemonData.observeAsState(initial = emptyList())
    if (pokemonList.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
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
                Card(
                    pokemon = p,

                    )
            }
        }


    }


}


@SuppressLint("InvalidColorHexValue")
@Composable
fun Card(pokemon: PokemonDTO) {
    Box(
        modifier = Modifier

            .fillMaxWidth()
            .background(Color(0xFFE0E0E0))
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
                //if(connection==null){
                // }
                AsyncImage(
                    model = pokemon.sprites,
                    contentDescription = pokemon.name,
                    modifier = Modifier
                        .height(40.dp).width(40.dp) // Adjusted size for image fitting
                        .clip(RoundedCornerShape(4.dp)), // Clip image for rounded corners
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Name and number of the Pokemon
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = pokemon.name)
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
                                .padding(horizontal = 6.dp, vertical = 2.dp) // Adjust padding for type label
                                .clip(RoundedCornerShape(4.dp))
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            }
        }    }
}

fun elementColor(type: String): Color {
    return when (type.lowercase()) {
        "normal" -> Color(0xFF9E9E9E) // Grayish neutral
        "fire" -> Color(0xFFFF5722) // Red-orange
        "water" -> Color(0xFF2196F3) // Deep blue
        "electric" -> Color(0xFFFFEB3B) // Bright yellow
        "grass" -> Color(0xFF4CAF50) // Fresh green
        "ice" -> Color(0xFF81D4FA) // Cool light blue
        "fighting" -> Color(0xFFB71C1C) // Strong red
        "poison" -> Color(0xFF9C27B0) // Purple
        "ground" -> Color(0xFFD7C297) // Earthy tan
        "flying" -> Color(0xFF90CAF9) // Soft light blue
        "psychic" -> Color(0xFFF06292) // Light pink
        "bug" -> Color(0xFF8BC34A) // Light green
        "rock" -> Color(0xFF795548) // Brown
        "ghost" -> Color(0xFF673AB7) // Dark purple
        "dragon" -> Color(0xFF1976D2) // Strong blue
        "dark" -> Color(0xFF212121) // Deep gray/black
        "steel" -> Color(0xFFB0BEC5) // Metallic gray
        "fairy" -> Color(0xFFF48FB1) // Light pink
        else -> Color.Gray // Default for unknown types
    }
}
