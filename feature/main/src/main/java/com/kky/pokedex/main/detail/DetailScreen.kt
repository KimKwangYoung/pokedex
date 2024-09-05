package com.kky.pokedex.main.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kky.pokedex.domain.model.PokemonDetail
import com.kky.pokedex.feature.common.theme.PokedexTheme
import com.kky.pokedex.feature.main.R
import com.kky.pokedex.main.component.InfoCard
import com.kky.pokedex.main.component.PokedexImage

const val POKEMON_ID = "id"

@Composable
fun DetailScreen(
    onBackButtonClick: () -> Unit,
    viewModel: PokemonDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val state = uiState

    PokedexTheme {
        when (state) {
            is PokemonDetailViewModel.UiState.Fail -> TODO()
            PokemonDetailViewModel.UiState.Loading -> DetailLoading()
            is PokemonDetailViewModel.UiState.Success -> DetailContent(
                data = state.data,
                onBackButtonClick = onBackButtonClick,
                onLikeClick = {
                    viewModel.toggleLike()
                },
            )
        }
    }
}

@Composable
private fun DetailLoading() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailContent(
    data: PokemonDetail,
    onBackButtonClick: () -> Unit,
    onLikeClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = data.name) },
                navigationIcon = {
                    IconButton(onClick = onBackButtonClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "",
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onLikeClick) {
                        Image(
                            painter = painterResource(
                                id = if (data.like) R.drawable.favorite_24dp
                                else R.drawable.favorite_border_24dp
                            ),
                            contentDescription = "",
                        )
                    }
                },
            )
        },
    ) { paddingValue ->
        Box(
            modifier = Modifier.padding(paddingValue)
        ) {
            LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
                item {
                    ContentHeader(data = data)
                }
                section(
                    "기본 스탯",
                    data.stat.map { stat -> "${stat.name}\n기본 스탯: ${stat.baseStat}" },
                )
                section(
                    "기본 능력",
                    data.ability,
                )
            }
        }
    }
}

@Composable
private fun ContentHeader(data: PokemonDetail) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        PokedexImage(
            imageUrl = data.imageUrl,
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = data.type.joinToString(", "))
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = data.description,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
    }
}

private fun LazyListScope.section(
    title: String,
    data: List<String>,
) {
    item {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
    }
    items(data) { info ->
        InfoCard(text = info)
    }
}
