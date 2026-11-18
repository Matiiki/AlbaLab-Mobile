package com.example.albalab.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AlbaLightColorScheme = lightColorScheme(
    primary = AlbaAzulPrimario,
    onPrimary = AlbaWhite,

    primaryContainer = AlbaCelesteClaro,
    onPrimaryContainer = AlbaAzulPrimario,

    secondary = AlbaAzulMedio,
    onSecondary = AlbaWhite,

    secondaryContainer = AlbaYellowLight,
    onSecondaryContainer = AlbaText,

    tertiary = AlbaAzulMedio,
    onTertiary = AlbaWhite,

    tertiaryContainer = AlbaCelesteClaro,
    onTertiaryContainer = AlbaAzulPrimario,

    background = AlbaCelesteFondo,
    onBackground = AlbaText,

    surface = AlbaWhite,
    onSurface = AlbaText,

    surfaceVariant = AlbaCelesteClaro,
    onSurfaceVariant = AlbaAzulMedio,

    outline = AlbaAzulMedio,

    error = AlbaRojoError,
    onError = AlbaWhite
)

private val AlbaDarkColorScheme = darkColorScheme(
    primary = AlbaDarkPrimary,
    onPrimary = AlbaDarkOnPrimary,

    primaryContainer = AlbaDarkPrimaryContainer,
    onPrimaryContainer = AlbaDarkOnPrimaryContainer,

    inversePrimary = AlbaAzulPrimario,

    secondary = AlbaDarkSecondary,
    onSecondary = AlbaDarkOnPrimary,

    secondaryContainer = AlbaDarkYellowContainer,
    onSecondaryContainer = AlbaDarkOnYellowContainer,

    tertiary = AlbaDarkSecondary,
    onTertiary = AlbaDarkOnPrimary,

    tertiaryContainer = AlbaDarkPrimaryContainer,
    onTertiaryContainer = AlbaDarkOnPrimaryContainer,

    background = AlbaDarkBackground,
    onBackground = AlbaDarkOnSurface,

    surface = AlbaDarkSurface,
    onSurface = AlbaDarkOnSurface,

    surfaceVariant = AlbaDarkSurfaceVariant,
    onSurfaceVariant = AlbaDarkOnSurfaceVariant,

    surfaceTint = AlbaDarkPrimary,
    outline = AlbaDarkOutline,
    outlineVariant = AlbaDarkSurfaceVariant,

    error = AlbaDarkError,
    onError = AlbaDarkOnError,
    errorContainer = Color(0xFF5C1A14),
    onErrorContainer = Color(0xFFFFDAD5)
)

@Composable
fun AlbaLabTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) AlbaDarkColorScheme else AlbaLightColorScheme,
        typography = Typography,
        content = content
    )
}
