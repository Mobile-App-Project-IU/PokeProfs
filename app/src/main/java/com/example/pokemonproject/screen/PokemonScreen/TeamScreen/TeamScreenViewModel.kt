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

@HiltViewModel
class TeamScreenViewModel @Inject constructor(
    private val repository: TeamRepository // Injecting the TeamRepository
) : ViewModel() {

    private val _teams = MutableStateFlow<List<Team>>(emptyList())
    val teams: StateFlow<List<Team>> get() = _teams

    init {
        loadTeams()
    }

    // Load teams from repository
    fun loadTeams() {
        viewModelScope.launch {
            repository.getAllTeams().collect { teamEntities ->
                _teams.value = teamEntities.map { it.toDomain() } // Convert TeamEntity to Team
            }
        }
    }

    // Function to delete a team
    fun deleteTeam(team: Team) {
        viewModelScope.launch {
            try {
                // Delete the team using the repository
                repository.deleteTeam(team.toEntity()) // Assuming `toEntity()` converts `Team` to `TeamEntity`
                Log.d("TeamScreenViewModel", "Team deleted successfully: ${team.name}")

                // Refresh the teams list after deletion
                loadTeams()
            } catch (e: Exception) {
                Log.e("TeamScreenViewModel", "Error deleting team: ${e.message}")
            }
        }
    }
}
