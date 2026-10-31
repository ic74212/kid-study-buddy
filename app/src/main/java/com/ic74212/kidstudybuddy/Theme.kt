package com.ic74212.kidstudybuddy

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Orange = Color(0xFFFF8A3D)
val Green = Color(0xFF43A047)
val Red = Color(0xFFE53935)
val Cream = Color(0xFFFFF8EC)

private val KidColors = lightColorScheme(
    primary = Orange,
    onPrimary = Color.White,
    secondary = Color(0xFF42A5F5),
    onSecondary = Color.White,
    tertiary = Green,
    background = Cream,
    onBackground = Color(0xFF3E2723),
    surface = Color.White,
    onSurface = Color(0xFF3E2723),
)

@Composable
fun KidTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = KidColors, content = content)
}
