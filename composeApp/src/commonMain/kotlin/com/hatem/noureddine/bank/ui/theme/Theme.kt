package com.hatem.noureddine.bank.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme =
    lightColorScheme(
        primary = ColorPrimary,
        onPrimary = Color.White,
        primaryContainer = ColorPrimaryVariant,
        onPrimaryContainer = Color.White,
        secondary = ColorSecondary,
        onSecondary = Color.White,
        tertiary = ColorTertiary,
        onTertiary = Color.White,
        background = ColorBackgroundLight,
        onBackground = ColorTextPrimaryLight,
        surface = ColorSurfaceLight,
        onSurface = ColorTextPrimaryLight,
        surfaceVariant = ColorSurfaceVariantLight,
        onSurfaceVariant = ColorTextSecondaryLight,
        error = ColorError,
        onError = Color.White,
    )

private val DarkColorScheme =
    darkColorScheme(
        primary = ColorPrimary,
        onPrimary = Color.White,
        primaryContainer = ColorPrimaryVariant,
        onPrimaryContainer = Color.White,
        secondary = ColorSecondary,
        onSecondary = Color.White,
        tertiary = ColorTertiary,
        onTertiary = Color.Black,
        background = ColorBackgroundDark,
        onBackground = ColorTextPrimaryDark,
        surface = ColorSurfaceDark,
        onSurface = ColorTextPrimaryDark,
        surfaceVariant = ColorSurfaceVariantDark,
        onSurfaceVariant = ColorTextSecondaryDark,
        error = ColorError,
        onError = Color.White,
    )

/**
 * Main Theme for the Bank App.
 * Applies the defined color scheme and typography to the content.
 *
 * @param darkTheme True if the system is in dark mode, false otherwise.
 * @param content The composable content to apply the theme to.
 */
@Composable
fun BankTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}
