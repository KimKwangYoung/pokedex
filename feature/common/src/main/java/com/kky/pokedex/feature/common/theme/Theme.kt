package com.kky.pokedex.feature.common.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

val NoneThemeColor = lightColorScheme(
    background = White,
    surface = White,
    onSurface = Black,
    surfaceVariant = Pink,
    secondary = LightGray,
    onSecondary = Black,
    primary = Red,
    outline = DarkGray
)

@Composable
fun PokedexTheme(
    content: @Composable  () -> Unit
) {
    MaterialTheme(
        colorScheme = NoneThemeColor,
        content = content
    )
}
