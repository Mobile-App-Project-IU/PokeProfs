import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.myapplication.R
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
    pokemonImageSize: Dp = 200.dp
) {
    LaunchedEffect(key1 = true) {
        viewModel.fetchPokemon(id)
    }
    val pokemonState by viewModel.pokemonState.observeAsState(initial = PokemonState())
    val pokemon = pokemonState.pokemon ?: PokemonDTO(types = listOf())

    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
                .padding(top = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (pokemonState.status) {
                PokemonStatus.LOADING -> {
                    Text("Loading...", style = MaterialTheme.typography.titleLarge)
                }

                PokemonStatus.ERROR -> {
                    Text("Error loading Pokemon details.", color = Color.Red, style = MaterialTheme.typography.titleLarge)
                }

                PokemonStatus.SUCCESS -> {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                DetailRow(label = "National ID", value = pokemon.id.toString())
                                DetailRow(label = "Name", value = pokemon.name.replaceFirstChar { it.uppercaseChar() })
                                DetailRow(label = "Height", value = pokemon.height)
                                DetailRow(label = "Weight", value = pokemon.weight)
                            }

                            AsyncImage(
                                model = pokemon.sprites,
                                contentDescription = "Pokemon Sprite",
                                modifier = Modifier
                                    .size(pokemonImageSize)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Types",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            pokemon.types.forEach { type ->
                                TypeChip(type = type)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Base Stats",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        StatsGrid(
                            hp = pokemon.hp,
                            attack = pokemon.attack,
                            defense = pokemon.defense,
                            specialAttack = pokemon.special_attack,
                            specialDefense = pokemon.special_defense,
                            speed = pokemon.speed
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Description",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 10.dp
                            )
                        ) {
                            Text(
                                text = pokemon.description,
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                PokemonStatus.INIT -> {}
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.width(200.dp).padding(vertical = 4.dp, horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Start)
        Text(value, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.End)
    }
}

@Composable
fun TypeChip(type: String) {
    Box(
        modifier = Modifier
            .background(
                color = elementColor(type),
                shape = MaterialTheme.shapes.small
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = type.replaceFirstChar { it.uppercaseChar() },
            color = Color.White,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun StatsGrid(hp: String, attack: String, defense: String, specialAttack: String, specialDefense: String, speed: String) {
    Column {
        val statsPairs = listOf(
            "HP" to hp,
            "Attack" to attack,
            "Defense" to defense,
            "SP. Attack" to specialAttack,
            "SP. Defense" to specialDefense,
            "Speed" to speed
        )

        statsPairs.chunked(2).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                row.forEach { (label, value) ->
                    Text("$label: $value", modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

//@Composable
//fun PokemonTypesRow(types: List<String>) {
//    Row(
//        horizontalArrangement = Arrangement.spacedBy(8.dp), // Space between items
//        verticalAlignment = Alignment.CenterVertically,
//        modifier = Modifier.padding(16.dp) // Padding around the Row
//    ) {
//        types.forEach { type ->
//            Text(
//                text = type.replaceFirstChar { it.uppercaseChar() }, // Capitalize first letter
//                style = TextStyle(
//                    color = Color.White,
//                    fontSize = 16.sp
//                ),
//                modifier = Modifier
//                    .background(
//                        color = elementColor(type),
//                        shape = RoundedCornerShape(50) // Rounded pill shape
//                    )
//                    .padding(horizontal = 16.dp, vertical = 8.dp) // Inner padding
//            )
//        }
//    }
//}
