package com.kky.pokedex.feature.common.theme

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

val NoneThemeColor = lightColorScheme(
    background = White,
    surface = White,
    onSurface = Black,
    surfaceContainerHighest = Pink,
    secondary = LightGray,
    onSecondary = Black,
    primary = Red,
    outline = DarkGray
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokedexTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalRippleConfiguration provides null
    ) {
        MaterialTheme(
            colorScheme = NoneThemeColor,
            content = content
        )
    }
}
