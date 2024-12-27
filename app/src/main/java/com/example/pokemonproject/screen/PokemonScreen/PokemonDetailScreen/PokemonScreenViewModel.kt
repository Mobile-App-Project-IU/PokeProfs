package com.example.pokemonproject.screen.PokemonScreen.PokemonDetailScreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokemonproject.domain.model.PokemonState
import com.example.pokemonproject.domain.model.PokemonStatus
import com.example.pokemonproject.domain.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonScreenViewModel @Inject constructor (private val repository: PokemonRepository) : ViewModel() {
    private val _pokemonState = MutableLiveData(PokemonState())
    val pokemonState: LiveData<PokemonState> = _pokemonState
    suspend fun fetchPokemon(pokemonID: Int) {
        _pokemonState.value = _pokemonState.value?.copy(
            status = PokemonStatus.LOADING
        )
        try {
            val pokemonDTO = repository.getLocalPokemonDataById(pokemonID)
            Log.d("PokemonScreenViewModel", "fetchPokemon: $pokemonDTO")
            _pokemonState.value = _pokemonState.value?.copy(
                status = PokemonStatus.SUCCESS,
                pokemon = pokemonDTO
            )
        } catch (e: Exception) {
            _pokemonState.value = _pokemonState.value?.copy(
                status = PokemonStatus.ERROR
            )
        }

    }
}