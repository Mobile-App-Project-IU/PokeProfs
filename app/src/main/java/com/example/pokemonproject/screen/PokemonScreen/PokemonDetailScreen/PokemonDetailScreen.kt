import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.myapplication.R
import com.example.myapplication.ui.theme.AguDisplay
import com.example.myapplication.ui.theme.elementColor
import com.example.myapplication.ui.theme.elementIcon
import com.example.pokemonproject.domain.DTO.PokemonDTO
import com.example.pokemonproject.domain.model.PokemonState
import com.example.pokemonproject.domain.model.PokemonStatus
import com.example.pokemonproject.screen.PokemonScreen.PokemonDetailScreen.PokemonScreenViewModel
import kotlinx.coroutines.Delay


@Composable
fun PokemonDetailScreen(
    viewModel: PokemonScreenViewModel = hiltViewModel(),
    id: Int,
    animatedVisibilityScope: AnimatedVisibilityScope,
    pokemonImageSize: Dp = 300.dp
) {
    LaunchedEffect(key1 = true) {
        viewModel.fetchPokemon(id)
    }
    val pokemonState by viewModel.pokemonState.observeAsState(initial = PokemonState())
    val pokemon = pokemonState.pokemon ?: PokemonDTO(types = listOf());
    val scrollState = rememberScrollState()


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color.White, // Starting color
                        pokemon.types.firstOrNull()?.let {
                            elementColor(it).copy(alpha = 1f)
                        } ?: Color.Gray.copy(alpha = 0.5f) // Fallback color if the first type is null
                    )
                )
            ),
        contentAlignment = Alignment.Center
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState) // Enable scrolling
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                        Box(
                            modifier = Modifier.height(70.dp)
                        )

                        Row (
                            horizontalArrangement = Arrangement.SpaceBetween, // Items spaced evenly between the start and end
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth() // Optional: Make the row take full width
                        ){
                            Text(
                                text = pokemon.name.replaceFirstChar { it.uppercaseChar() },
                                style = TextStyle(
                                    color = Color(0xFFC9B710),
                                    fontSize = 35.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = AguDisplay,
                                ),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Image(
                                painter = painterResource(id = elementIcon(pokemon.types[0])),
                                contentDescription = "Fire Icon",
                                modifier = Modifier
                                    .size(75.dp) // Adjust size as needed
                            )
                        }
                        // Display the image using Coil's AsyncImage
                        AsyncImage(
                            model = pokemon.sprites,
                            contentDescription = "Pokemon Image",
                            modifier = Modifier
                                .size(pokemonImageSize)
                        )
                        PokemonTypesRow(types = pokemon.types)
                        PokemonStat(
                            statName = "HP",
                            statValue = pokemon.hp.toInt(),
                            statMaxValue = 100,
                            statColor = Color.Green,
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        PokemonStat(
                            statName = "ATK",
                            statValue = pokemon.attack.toInt(),
                            statMaxValue = 100,
                            statColor = Color.Red,
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        PokemonStat(
                            statName = "DEF",
                            statValue = pokemon.defense.toInt(),
                            statMaxValue = 100,
                            statColor = Color(0xFF18aded),
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        PokemonStat(
                            statName = "SpAtk",
                            statValue = pokemon.special_attack.toInt(),
                            statMaxValue = 100,
                            statColor = Color(0xFF9512db),
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        PokemonStat(
                            statName = "SpDef",
                            statValue = pokemon.special_defense.toInt(),
                            statMaxValue = 100,
                            statColor = Color(0xFFd90cf0),
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                    }
                }

                PokemonStatus.INIT -> TODO()
            }
        }
    }
    // Display the name above the image
}

@Composable
fun PokemonTypesRow(types: List<String>) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp), // Space between items
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(16.dp) // Padding    around the Row
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
                        color = elementColor(type),
                        shape = RoundedCornerShape(50) // Rounded pill shape
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp) // Inner padding
            )
        }
    }
}


@Composable
fun PokemonStat(
    statName: String,
    statValue: Int,
    statMaxValue: Int,
    statColor: Color,
    height: Dp = 28.dp,
    animDuration: Int = 1000,
    animDelay: Int = 0
) {
    var animationPlayed by remember {
        mutableStateOf(false)
    }
    val curPercent = animateFloatAsState(
        targetValue = if(animationPlayed) {
            statValue / statMaxValue.toFloat()
        } else 0f,
        animationSpec = tween(
            animDuration,
            animDelay
        )
    )
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(CircleShape)
            .background(
                if (isSystemInDarkTheme()) {
                    Color(0xFF505050)
                } else {
                    Color.LightGray
                }
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(curPercent.value)
                .clip(CircleShape)
                .background(statColor)
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = statName,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = (curPercent.value * statMaxValue).toInt().toString(),
                fontWeight = FontWeight.Bold
            )
        }
    }
}