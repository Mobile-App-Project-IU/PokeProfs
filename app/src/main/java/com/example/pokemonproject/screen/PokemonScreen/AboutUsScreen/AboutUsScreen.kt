package com.example.pokemonproject.screen.PokemonScreen.AboutUsScreen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.pokemonproject.screen.PokemonScreen.PokemonAppBarWithMenu
import com.example.pokemonproject.screen.PokemonScreen.PokemonScreen

@Composable
fun AboutUsScreen(
    canNavigateBack: Boolean = false,
    navigateUp: () -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    // Data for the members and their emails
    val teamMembers = listOf(
        TeamMember("1", "Vo Hoai Bao", "ITITIU21038@hcmiu.edu.vn"),
        TeamMember("2", "Tran Bac Chuong", "ITITIU20093@hcmiu.edu.vn"),
        TeamMember("3", "Tram Le Manh", "ITITIU21077@hcmiu.edu.vn"),
        TeamMember("4", "Thai Thanh Phat", "ITITIU21274@hcmiu.edu.vn"),
        TeamMember("5", "Nguyen Quang Minh Tri", "ITITIU21140@hcmiu.edu.vn")
    )

    Scaffold(
        topBar = {
            PokemonAppBarWithMenu(
                pokemonScreen = PokemonScreen.AboutUSScreen, // Reusing the TopAppBar from PokemonScreen
                canNavigateBack = canNavigateBack,
                navigateUp = navigateUp,
                onMenuClick = onMenuClick,
                onFilterClick = {}
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // Header section
                Text(
                    text = "About Us",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "Project Motivation: We built this project to provide an engaging platform for Pokemon enthusiasts to manage their Pokemon teams and share knowledge. We aim to bring a fun and educational experience to all users.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // Table header
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "No",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "Name",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.weight(2f)
                    )
                    Text(
                        text = "Email",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.weight(2f)
                    )
                }

                // Divider
                Spacer(modifier = Modifier.height(8.dp))
                Divider()

                // Display team members
                teamMembers.forEach { member ->
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)) {
                        Text(
                            text = member.no,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = member.name,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(2f)
                        )
                        Text(
                            text = member.email,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(2f)
                        )
                    }
                    Divider()
                }
            }
        }
    )
}

data class TeamMember(
    val no: String,
    val name: String,
    val email: String
)

