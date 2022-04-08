package com.mobile.travelapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightThemeColors = lightColors(
    primary = DeepPurple700,
    primaryVariant = DeepPurple900,
    onPrimary = Color.White,
    secondary = DeepPurple700,
    secondaryVariant = DeepPurple900,
    onSecondary = Color.White,
    error = DeepPurple800,
    onBackground = Color.Black,
)

private val DarkThemeColors = darkColors(
    primary = DeepPurple300,
    primaryVariant = DeepPurple700,
    onPrimary = Color.Black,
    secondary = DeepPurple300,
    onSecondary = Color.Black,
    error = DeepPurple200,
    onBackground = Color.White
)

@Composable
fun TravelAppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkThemeColors
    } else {
        LightThemeColors
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
