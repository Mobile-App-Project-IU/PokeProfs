package com.example.pokemonproject.screen.PokemonScreen.PokemonList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonproject.domain.DTO.PokemonDTO
import com.example.pokemonproject.domain.repository.PokemonRepository
import com.example.pokemonproject.domain.model.PokemonState
import com.example.pokemonproject.domain.model.PokemonStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _pokemonState = MutableLiveData(PokemonState())
    val pokemonState: LiveData<PokemonState> = _pokemonState

    private var originalList: List<PokemonDTO> = emptyList()

    init {
        fetchPokemonData()
    }

    // Fetch Pokémon data
    fun fetchPokemonData() {
        updateState(PokemonStatus.LOADING)
        viewModelScope.launch {
            try {
                originalList = repository.getPokemonData()
                updateState(PokemonStatus.SUCCESS, pokemonList = originalList)
            } catch (e: Exception) {
                updateState(PokemonStatus.ERROR)
            }
        }
    }

    // Update state utility
    private fun updateState(
        status: PokemonStatus,
        pokemonList: List<PokemonDTO> = _pokemonState.value?.pokemonList ?: emptyList(),
        pokemon: PokemonDTO? = _pokemonState.value?.pokemon
    ) {
        _pokemonState.value = PokemonState(
            pokemonList = pokemonList,
            status = status,
            pokemon = pokemon
        )
    }

    // Dynamic search Pokémon
    fun searchPokemon(query: String?) {
        val filteredList = if (query.isNullOrBlank()) {
            originalList
        } else {
            originalList.filter { it.name.contains(query, ignoreCase = true) }
        }
        updateState(PokemonStatus.SUCCESS, pokemonList = filteredList)
    }

    // Filter Pokémon by type
    fun filterPokemonByType(type: String) {
        val filteredList = if (type.isEmpty()) {
            originalList // If the type is empty, return the original list
        } else {
            originalList.filter { it.types.contains(type) } // Otherwise, filter by type
        }
        updateState(PokemonStatus.SUCCESS, pokemonList = filteredList)
    }
}
