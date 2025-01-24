package com.appat.sendmoney.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val ColorScheme = darkColorScheme(
    background = background,
    surface = background,
    primary = primary,
    secondary = secondary,
    tertiary = tertiary,
    onPrimary = primaryText,
    onSecondary = secondaryText,
    onTertiary = tertiaryText,
    error = error,
)

@Composable
fun SendMoneyTheme(
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = ColorScheme,
        typography = Typography,
        content = content
    )
}