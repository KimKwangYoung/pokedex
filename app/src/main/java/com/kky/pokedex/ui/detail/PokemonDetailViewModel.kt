package com.kky.pokedex.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kky.pokedex.domain.model.PokemonDetail
import com.kky.pokedex.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: PokemonRepository
): ViewModel() {

    private val id: Int = savedStateHandle["id"] ?: throw NullPointerException("포켓몬 ID를 찾을 수 없습니다.")

    private val _state = MutableStateFlow<UiState>(UiState.Loading)
    val state: StateFlow<UiState> = _state

    private val _event = MutableSharedFlow<String>()
    val event: SharedFlow<String> = _event

    init {
        getPokemonDetail()
    }

    private fun getPokemonDetail() {
        viewModelScope.launch {
            runCatching {
                repository.getPokemonDetail(id)
            }.onFailure {
                _state.value = UiState.Fail(it.message ?: "정보를 불러오는 데 실패하였습니다.")
            }.onSuccess {
                _state.value = UiState.Success(data = it)
            }
        }
    }

    fun toggleLike() {
        if (_state.value !is UiState.Success) return

        val data = (_state.value as UiState.Success).data
        val isLike = data.like

        viewModelScope.launch {
            runCatching {
                if (isLike) {
                    repository.unlike(data.id)
                } else {
                    repository.like(data.id)
                }
            }.onFailure {
                _event.emit("실패하였습니다.")
            }.onSuccess {
                getPokemonDetail()
            }
        }
    }

    sealed interface UiState {
        object Loading: UiState

        data class Success(
            val data: PokemonDetail
        ): UiState

        data class Fail(
            val message: String
        ): UiState
    }
}