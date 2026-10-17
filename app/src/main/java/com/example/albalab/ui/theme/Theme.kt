package com.example.albalab.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val AlbaColorScheme = lightColorScheme(
    primary = AlbaBlue,
    onPrimary = AlbaWhite,

    primaryContainer = AlbaLightBlue,
    onPrimaryContainer = AlbaBlueDark,

    secondary = AlbaYellow,
    onSecondary = AlbaText,

    secondaryContainer = AlbaYellowLight,
    onSecondaryContainer = AlbaText,

    background = AlbaBackground,
    onBackground = AlbaText,

    surface = AlbaWhite,
    onSurface = AlbaText,

    error = AlbaError
)

@Composable
fun AlbaLabTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AlbaColorScheme,
        typography = Typography,
        content = content
    )
}