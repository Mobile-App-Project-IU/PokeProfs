package com.example.pokemonproject.screen.PokemonList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonproject.domain.DTO.PokemonDTO
import com.example.pokemonproject.domain.model.PokemonState
import com.example.pokemonproject.domain.model.PokemonStatus
import com.example.pokemonproject.domain.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor (private val repository: PokemonRepository) : ViewModel() {


    private val _pokemonState = MutableLiveData(PokemonState())
    val pokemonState: LiveData<PokemonState> = _pokemonState
    private var originalLList: List<PokemonDTO> = emptyList()


    init {
        fetchPokemonData()
    }
    private fun fetchPokemonData() {
        _pokemonState.value = _pokemonState.value?.copy(
            status = PokemonStatus.LOADING
        )
        viewModelScope.launch {
            try {
                originalLList = repository.fetchPokemonList()
                _pokemonState.value = _pokemonState.value?.copy(
                    status = PokemonStatus.SUCCESS, pokemonList = originalLList
                )
            } catch (e: Exception) {
                _pokemonState.value = _pokemonState.value?.copy(
                    status = PokemonStatus.ERROR
                )
            }
        }
    }
    fun searchPokemon(query: String?) {
        _pokemonState.value = _pokemonState.value?.copy(
            status = PokemonStatus.LOADING
        )
        val filteredList = if (query.isNullOrBlank()) {
            originalLList
        } else {
            originalLList.filter { it.name.equals(query, ignoreCase = true) }
        }
        _pokemonState.value = _pokemonState.value?.copy(
            status = PokemonStatus.SUCCESS, pokemonList = filteredList
        )
    }
}