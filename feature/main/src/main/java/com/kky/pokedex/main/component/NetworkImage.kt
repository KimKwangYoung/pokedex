package com.kky.pokedex.main.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun NetworkImage(
    imageUrl: String,
    modifier: Modifier
) {
    CoilImage(
        imageModel = { imageUrl },
        modifier = modifier,
    )
}