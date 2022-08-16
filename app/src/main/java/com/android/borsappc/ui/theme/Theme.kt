package com.android.borsappc.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    surface = Blue,
    onSurface = Navy,
    primary = Navy,
    onPrimary = Chartreuse
)

private val DraculaColorPalette = darkColors(
    primary = Pink,
    primaryVariant = Purple,
    secondary = Green,
    secondaryVariant = Orange,
    background = Black,
    surface = Black,
    error = Red,
    onPrimary = White,
    onSecondary = White,
    onError = White,
    onSurface = White,
    onBackground = White
)

private val LightColorPalette = lightColors()

@Composable
fun BorsaPpcTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    var colors = if (darkTheme) {
        DraculaColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(colors = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}