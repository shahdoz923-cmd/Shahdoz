package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = NexoraSecondary,
    onPrimary = Color.Black,
    primaryContainer = NexoraPrimaryDark,
    onPrimaryContainer = Color.White,
    secondary = NexoraSecondary,
    onSecondary = Color.Black,
    tertiary = NexoraAccentPink,
    background = NexoraBackgroundDark,
    onBackground = NexoraTextPrimaryDark,
    surface = NexoraSurfaceDark,
    onSurface = NexoraTextPrimaryDark,
    surfaceVariant = NexoraSurfaceVariantDark,
    onSurfaceVariant = NexoraTextSecondaryDark,
    outline = NexoraBorderDark
)

private val LightColorScheme = lightColorScheme(
    primary = NexoraPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEDE9FE),
    onPrimaryContainer = NexoraPrimaryDark,
    secondary = NexoraSecondary,
    onSecondary = Color.White,
    tertiary = NexoraAccentPink,
    background = NexoraBackgroundLight,
    onBackground = NexoraTextPrimaryLight,
    surface = NexoraSurfaceLight,
    onSurface = NexoraTextPrimaryLight,
    surfaceVariant = NexoraSurfaceVariantLight,
    onSurfaceVariant = NexoraTextSecondaryLight,
    outline = NexoraBorderLight
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep distinct NEXORA branding consistent
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
