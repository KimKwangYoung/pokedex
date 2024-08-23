package com.kky.pokedex.main.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kky.pokedex.domain.model.Pokemon
import com.kky.pokedex.feature.main.R

@Composable
fun PokemonListItem(
    pokemon: Pokemon,
    showOnlyLike: Boolean,
    onClickItem: () -> Unit,
    onClickLike: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { onClickItem() }
            .padding(16.dp),
    ) {
        PokedexImage(
            imageUrl = pokemon.imageUrl,
            modifier = Modifier.size(60.dp)
        )
        Spacer(modifier = Modifier.size(10.dp))
        PokemonDescription(
            pokemon = pokemon,
            showOnlyLike = showOnlyLike,
            modifier = Modifier.weight(1.0f)
        )
        Image(
            painter = painterResource(
                id = if (pokemon.like) R.drawable.favorite_24dp
                else R.drawable.favorite_border_24dp,
            ),
            contentDescription = "",
            modifier = Modifier.clickable {
                onClickLike()
            },
        )
    }
}

@Composable
private fun PokemonDescription(
    pokemon: Pokemon,
    showOnlyLike: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = pokemon.name,
            style = MaterialTheme.typography.bodyMedium,
        )
        if (showOnlyLike) {
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
    }
}