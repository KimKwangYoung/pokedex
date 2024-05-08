package com.example.first_week_mission.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.first_week_mission.ui.model.PokemonUiModel
import com.example.first_week_mission.repository.PokemonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import java.util.ArrayList

class MainViewModel : ViewModel() {
    private var loaded: Int = 0

    private var data = emptyList<PokemonUiModel>()

    private val filteredData: List<PokemonUiModel>
        get() = if (showOnlyLike) data.filter { it.like } else data

    private val _dataFlow: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState.Loading)
    val dataFlow: StateFlow<MainUiState> = _dataFlow

    private var showOnlyLike = false

    fun loadPokemon() {
        val start = loaded + 1

        loaded += LOAD_COUNT

        if (start >= MAX_COUNT) {
            return
        }

        val destination = if (loaded >= MAX_COUNT) 151 else loaded

        viewModelScope.launch {

            _dataFlow.emit(MainUiState.Loading)

            runCatching {
                val load = PokemonRepository.loadPokemon(start, destination)
                val new = ArrayList(data)
                new.addAll(load)
                data = new

                _dataFlow.emit(
                    MainUiState.Success(
                        data = filteredData,
                        showOnlyLike = showOnlyLike
                    )
                )
            }.onFailure {
                _dataFlow.emit(MainUiState.Fail("데이터를 불러오는 데 실패하였습니다."))
            }
        }
    }

    fun setLike(pokemon: PokemonUiModel, like: Boolean) {
        val current = ArrayList(data)
        val index = current.indexOf(pokemon)
        current[index] = pokemon.copy(like = like)

        data = current

        Log.d("MainViewModel", "setLike $current")

        viewModelScope.launch {
            _dataFlow.emit(
                MainUiState.Success(
                    data = filteredData,
                    showOnlyLike = showOnlyLike
                )
            )
        }
    }

    fun showOnlyLike(arg: Boolean) {
        if (showOnlyLike != arg) {
            showOnlyLike = arg

            viewModelScope.launch {
                _dataFlow.emit(
                    MainUiState.Success(
                        data = filteredData,
                        showOnlyLike = arg
                    )
                )
            }
        }
    }

    sealed interface MainUiState {
        data object Loading : MainUiState

        data class Success(
            val data: List<PokemonUiModel>,
            val showOnlyLike: Boolean
        ) : MainUiState {
            override fun equals(other: Any?): Boolean {
                return super.equals(other)
            }

            override fun hashCode(): Int {
                return super.hashCode()
            }
        }

        data class Fail(
            val errorMessage: String
        ) : MainUiState
    }

    companion object {
        //1세대 포켓몬만 불러올 수 있다.
        const val MAX_COUNT = 151

        //한번에 불러올 수 있는 포켓몬 정보 갯수
        const val LOAD_COUNT = 10
    }
}