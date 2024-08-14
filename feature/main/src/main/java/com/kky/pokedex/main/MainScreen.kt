package com.kky.pokedex.main

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kky.pokedex.domain.model.Pokemon
import com.kky.pokedex.feature.common.extension.noEffectClickable
import com.kky.pokedex.feature.common.theme.PokedexTheme
import com.kky.pokedex.feature.main.R
import com.kky.pokedex.main.component.NetworkImage
import com.kky.pokedex.main.component.PagingLazyColumn
import com.kky.pokedex.main.detail.PokemonDetailActivity

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
) {
    val uiState by viewModel.dataFlow.collectAsStateWithLifecycle()
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
            val showOnlyLike =
                (uiState as? MainViewModel.MainUiState.Success)?.showOnlyLike ?: false

            PokedexTheme {
                Scaffold(topBar = { MainTopBar() }) { paddingValue ->
                    Box(
                        modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
                    ) {
                        Column(
                            modifier = Modifier.padding(paddingValue),
                        ) {
                            LikeFilterBar(
                                checked = (uiState as? MainViewModel.MainUiState.Success)?.showOnlyLike
                                    ?: false,
                                onCheckedChange = { checked -> viewModel.showOnlyLike(checked) },
                            )
                            MainPokemonList(
                                data = (uiState as? MainViewModel.MainUiState.Success)?.data
                                    ?: emptyList(),
                                showOnlyLike = showOnlyLike,
                                onClickItem = { pokemon ->
                                    context.startActivity(
                                        Intent(
                                            context,
                                            PokemonDetailActivity::class.java
                                        ).putExtra(
                                            "id",
                                            pokemon.id
                                        )
                                    )
                                },
                                onClickLike = { pokemon, like ->
                                    viewModel.setLike(
                                        pokemon,
                                        like
                                    )
                                },
                                loadAction = viewModel::loadPokemon,
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar() {
    TopAppBar(
        title = {
            Text(
                text = "1세대 포켓몬 도감",
                style = MaterialTheme.typography.titleMedium,
            )
        },
    )
}

@Composable
fun LikeFilterBar(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Surface(
        color = MaterialTheme.colorScheme.secondary,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(
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
                colors = SwitchDefaults.colors(
                    checkedTrackColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    }
}

@Composable
fun MainPokemonList(
    data: List<Pokemon>,
    showOnlyLike: Boolean,
    onClickItem: (Pokemon) -> Unit,
    onClickLike: (Pokemon, Boolean) -> Unit,
    loadAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (showOnlyLike) {
        LazyColumn(modifier = modifier) {
            items(data) { pokemon ->
                PokemonLikeListItem(
                    pokemon = pokemon,
                    onClickItem = onClickItem,
                    onClickLike = onClickLike,
                )
            }
        }
    } else {

        PagingLazyColumn(
            reachedEnd = data.size == 151,
            loadAction = loadAction,
        ) {
            items(
                data,
                key = { pokemon -> pokemon.id },
            ) { pokemon ->
                PokemonListItem(
                    pokemon = pokemon,
                    onClickItem = onClickItem,
                    onClickLike = onClickLike,
                )
            }
        }
    }
}

@Composable
fun PokemonLikeListItem(
    pokemon: Pokemon,
    onClickItem: (Pokemon) -> Unit,
    onClickLike: (Pokemon, Boolean) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { onClickItem(pokemon) }
            .padding(16.dp),
    ) {
        NetworkImage(
            imageUrl = pokemon.imageUrl,
            modifier = Modifier.size(60.dp)
        )
        Spacer(modifier = Modifier.size(10.dp))
        Column(
            modifier = Modifier.weight(1.0f),
        ) {
            Text(
                text = pokemon.name,
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.size(5.dp))
            Text(
                text = "타입: ${pokemon.type.joinToString(", ")}",
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.size(5.dp))
            Text(
                text = pokemon.description,
                style = MaterialTheme.typography.bodyMedium,
            )

        }
        Image(
            painter = painterResource(
                id = if (pokemon.like) R.drawable.favorite_24dp
                else R.drawable.favorite_border_24dp,
            ),
            contentDescription = "",
            modifier = Modifier.noEffectClickable {
                onClickLike(
                    pokemon,
                    !pokemon.like
                )
            },
        )
    }
}

@Composable
fun PokemonListItem(
    pokemon: Pokemon,
    onClickItem: (Pokemon) -> Unit,
    onClickLike: (Pokemon, Boolean) -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .noEffectClickable { onClickItem(pokemon) }
            .padding(16.dp)) {
        NetworkImage(
            imageUrl = pokemon.imageUrl,
            modifier = Modifier.size(60.dp)
        )
        Spacer(modifier = Modifier.size(10.dp))
        Text(
            text = pokemon.name,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1.0f),
        )
        Image(
            painter = painterResource(
                id = if (pokemon.like) R.drawable.favorite_24dp
                else R.drawable.favorite_border_24dp,
            ),
            contentDescription = "",
            modifier = Modifier.noEffectClickable {
                onClickLike(
                    pokemon,
                    !pokemon.like
                )
            },
        )
    }
}
