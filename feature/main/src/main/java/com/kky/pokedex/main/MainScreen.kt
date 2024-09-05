package com.kky.pokedex.main

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kky.pokedex.domain.model.Pokemon
import com.kky.pokedex.feature.common.theme.PokedexTheme
import com.kky.pokedex.main.component.PagingLazyColumn
import com.kky.pokedex.main.component.PokemonListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onClickItem: (Pokemon) -> Unit,
    viewModel: MainViewModel = hiltViewModel(),
) {
    val state by viewModel.dataFlow.collectAsStateWithLifecycle()
    val uiState = state
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect {
            when (it) {
                is MainViewModel.MainEvent.Fail -> {
                    Toast.makeText(
                        context,
                        it.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    when (uiState) {
        is MainViewModel.MainUiState.Success -> {
            val showOnlyLike = uiState.showOnlyLike
            Scaffold(topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "1세대 포켓몬 도감",
                            style = MaterialTheme.typography.titleMedium,
                        )
                    },
                )
            }) { paddingValue ->
                Box(
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.background)
                        .padding(paddingValue),
                ) {
                    Column {
                        LikeFilterBar(
                            checked = uiState.showOnlyLike,
                            onCheckedChange = { checked -> viewModel.showOnlyLike(checked) },
                        )
                        MainPokemonList(data = uiState.data,
                            showOnlyLike = showOnlyLike,
                            loadAction = viewModel::loadPokemon,
                            onClickItem = onClickItem,
                            onClickLike = { pokemon ->
                                viewModel.setLike(
                                    pokemon,
                                    !pokemon.like
                                )
                            })
                    }
                }
            }
        }
    }
}

@Composable
fun LikeFilterBar(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.secondary)
            .padding(
                horizontal = 16.dp,
                vertical = 10.dp,
            )
    ) {
        Text(
            text = "좋아요만 보기",
            style = MaterialTheme.typography.titleSmall,
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors().copy(
                uncheckedTrackColor = MaterialTheme.colorScheme.outline,
                uncheckedBorderColor = MaterialTheme.colorScheme.outline,
                uncheckedThumbColor = MaterialTheme.colorScheme.surface,
            )
        )
    }
}

@Composable
fun MainPokemonList(
    data: List<Pokemon>,
    showOnlyLike: Boolean,
    loadAction: () -> Unit,
    onClickLike: (Pokemon) -> Unit,
    onClickItem: (Pokemon) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listItem: LazyListScope.() -> Unit = {
        items(
            data,
            key = { pokemon -> pokemon.id },
        ) { pokemon ->
            PokemonListItem(
                pokemon = pokemon,
                showOnlyLike = showOnlyLike,
                onClickItem = { onClickItem(pokemon) },
                onClickLike = { onClickLike(pokemon) },
            )
        }
    }

    if (showOnlyLike) {
        LazyColumn(
            content = listItem,
            modifier = modifier,
        )
    } else {
        PagingLazyColumn(
            reachedEnd = data.size == 151,
            loadAction = loadAction,
            content = listItem,
            modifier = modifier,
        )
    }
}
