package com.example.pokemonproject.screen.PokemonScreen.TeamScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonproject.data.Room.Entity.toDomain
import com.example.pokemonproject.data.Room.Entity.toEntity
import com.example.pokemonproject.domain.model.Team
import com.example.pokemonproject.domain.repository.TeamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// ViewModel for the Team Screen, responsible for managing UI-related data
@HiltViewModel
class TeamScreenViewModel @Inject constructor(
    private val repository: TeamRepository // Injecting the TeamRepository to interact with data layer
) : ViewModel() {

    // Mutable state flow to hold the list of teams, private to the ViewModel
    private val _teams = MutableStateFlow<List<Team>>(emptyList())

    // Public state flow to expose the list of teams to the UI
    val teams: StateFlow<List<Team>> get() = _teams

    // Initialization block: loads the teams when the ViewModel is created
    init {
        loadTeams()
    }

    // Function to load teams from the repository
    fun loadTeams() {
        viewModelScope.launch {
            // Collecting the team entities from the repository
            repository.getAllTeams().collect { teamEntities ->
                // Converting team entities to domain model objects and updating the state flow
                _teams.value = teamEntities.map { it.toDomain() }
            }
        }
    }

    // Function to delete a team
    fun deleteTeam(team: Team) {
        viewModelScope.launch {
            try {
                // Deleting the team through the repository, converting the Team object to TeamEntity
                repository.deleteTeam(team.toEntity()) // Assuming `toEntity()` converts `Team` to `TeamEntity`

                // Log success message
                Log.d("TeamScreenViewModel", "Team deleted successfully: ${team.name}")

                // Refresh the list of teams after deletion
                loadTeams()
            } catch (e: Exception) {
                // Log an error message if there is an exception during the deletion process
                Log.e("TeamScreenViewModel", "Error deleting team: ${e.message}")
            }
        }
    }
}
