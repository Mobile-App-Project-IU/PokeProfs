package com.example.pokemonproject.screen.PokemonScreen.CreateTeamScreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonproject.data.Room.Entity.toEntity
import com.example.pokemonproject.domain.DTO.PokemonDTO
import com.example.pokemonproject.domain.model.Team
import com.example.pokemonproject.domain.repository.PokemonRepository
import com.example.pokemonproject.domain.repository.TeamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateTeamViewModel @Inject constructor(
    private val teamRepository: TeamRepository,
    private val pokemonRepository: PokemonRepository
) : ViewModel() {

    private val _teamSaveStatus = MutableLiveData<TeamSaveStatus>()
    val teamSaveStatus: LiveData<TeamSaveStatus> get() = _teamSaveStatus

    private val _pokemonList = MutableLiveData<List<PokemonDTO>>()
    val pokemonList: LiveData<List<PokemonDTO>> get() = _pokemonList

    private var originalPokemonList: List<PokemonDTO> = emptyList()

    // Enum class for save team status (success, error)
    enum class TeamSaveStatus {
        SUCCESS, ERROR
    }

    // Fetch Pokémon list
    fun fetchPokemonData() {
        viewModelScope.launch {
            try {
                originalPokemonList = pokemonRepository.getPokemonData() // Fetch all available Pokémon
                _pokemonList.value = originalPokemonList // Update the list to be observed by UI
            } catch (e: Exception) {
                Log.e("CreateTeamViewModel", "Error fetching Pokémon data: ${e.message}")
            }
        }
    }

    // Search Pokémon by query
    fun searchPokemon(query: String?) {
        val filteredList = if (query.isNullOrBlank()) {
            originalPokemonList // If the query is empty, return the original list
        } else {
            originalPokemonList.filter { it.name.contains(query, ignoreCase = true) } // Filter by name
        }
        _pokemonList.value = filteredList // Update the filtered list in LiveData
    }

    fun getPokemonIdByName(name: String?): Int {
        // Ensure that the pokemonList is populated before accessing it
        val list = _pokemonList.value
        return list?.firstOrNull { it.name.equals(name, ignoreCase = true) }?.id ?: -1
    }



    // Save the team
    fun saveTeam(team: Team) {
        viewModelScope.launch {
            try {
                // Convert Team to TeamEntity and insert into the database
                teamRepository.insertTeam(team.toEntity()) // Convert Team to TeamEntity
                _teamSaveStatus.value = TeamSaveStatus.SUCCESS
            } catch (e: Exception) {
                // Handle any errors and notify the UI
                Log.e("CreateTeamViewModel", "Error saving team: ${e.message}")
                _teamSaveStatus.value = TeamSaveStatus.ERROR
            }
        }
    }
}
