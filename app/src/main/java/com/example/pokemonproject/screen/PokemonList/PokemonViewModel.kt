package com.example.pokemonproject.screen.PokemonList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonproject.data.network.DTO.PokemonDTO
import com.example.pokemonproject.domain.repository.PokemonRepository
import com.example.pokemonproject.data.repository.PokemonRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor (private val repository: PokemonRepository ) : ViewModel() {
    private val _pokemonData = MutableLiveData<List<PokemonDTO>>()
    val pokemonData: LiveData<List<PokemonDTO>> = _pokemonData
    init {
        viewModelScope.launch {
            _pokemonData.value = repository.fetchPokemonList();
        }
    }
    fun fetchPokemonData(pokemonName: String) {
        viewModelScope.launch {
            _pokemonData.value = repository.fetchPokemonList()
        }
    }
}