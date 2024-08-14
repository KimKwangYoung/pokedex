package com.kky.pokedex.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kky.pokedex.data.repository.PokemonRepository
import com.kky.pokedex.domain.model.Pokemon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {

    private var showOnlyLike = false

    private val _dataFlow: MutableStateFlow<MainUiState>
    = MutableStateFlow(MainUiState.Success(data = emptyList(), showOnlyLike = showOnlyLike))
    val dataFlow: StateFlow<MainUiState> = _dataFlow.asStateFlow()

    private val _effect: MutableSharedFlow<MainEvent> = MutableSharedFlow(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val effect: SharedFlow<MainEvent> = _effect.asSharedFlow()

    init {
        pokemonRepository.flowPokemon().onEach {
                _dataFlow.value = MainUiState.Success(
                    data = it.toImmutableList(),
                    showOnlyLike = showOnlyLike
                )
            }.launchIn(viewModelScope)
    }

    fun loadPokemon() {
        viewModelScope.launch {
            runCatching {
                pokemonRepository.loadPokemon()
            }.onFailure {
                _effect.tryEmit(
                    MainEvent.Fail(
                        message = "데이터를 불러오는 데 실패하였습니다."
                    )
                )
            }
        }
    }

    fun setLike(pokemon: Pokemon, like: Boolean) {
        viewModelScope.launch {
            runCatching {
                if (like) {
                    pokemonRepository.like(pokemon.id)
                } else {
                    pokemonRepository.unlike(pokemon.id)
                }
            }.onFailure {
                it.printStackTrace()
            }.onSuccess {
                Log.d(
                    "MainViewModel",
                    "갱신 성공"
                )
            }
        }
    }

    fun showOnlyLike(arg: Boolean) {
        showOnlyLike = arg
        pokemonRepository.setShowOnlyLike(arg)
    }

    sealed interface MainUiState {
        data class Success(
            val data: List<Pokemon>, val showOnlyLike: Boolean
        ) : MainUiState
    }

    sealed interface MainEvent {
        data class Fail(val message: String) : MainEvent
    }
}
