package com.example.fridgeai_android.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val EarthColorScheme = lightColorScheme(
    primary = FridgeGreen,
    onPrimary = FridgeL0,
    primaryContainer = FridgeGreenL,
    onPrimaryContainer = FridgeGreen,
    secondary = FridgeInk2,
    onSecondary = FridgeL0,
    secondaryContainer = FridgeL1,
    onSecondaryContainer = FridgeInk2,
    tertiary = FridgeAmber,
    onTertiary = FridgeL0,
    tertiaryContainer = FridgeAmberL,
    onTertiaryContainer = FridgeAmber,
    error = FridgeRed,
    onError = FridgeL0,
    errorContainer = FridgeRedL,
    onErrorContainer = FridgeRed,
    background = FridgeBg,
    onBackground = FridgeInk,
    surface = FridgeL0,
    onSurface = FridgeInk,
    surfaceVariant = FridgeL1,
    onSurfaceVariant = FridgeInk2,
    outline = FridgeBorder,
    outlineVariant = FridgeBorder2,
    inverseSurface = FridgeInk,
    inverseOnSurface = FridgeL0,
    scrim = Color.Black.copy(alpha = 0.25f)
)

private val EarthDarkColorScheme = darkColorScheme(
    primary = FridgeGreen,
    onPrimary = FridgeL0,
    primaryContainer = FridgeGreenL,
    onPrimaryContainer = FridgeGreen,
    secondary = FridgeInk2,
    onSecondary = FridgeL0,
    secondaryContainer = FridgeL1,
    onSecondaryContainer = FridgeInk2,
    tertiary = FridgeAmber,
    onTertiary = FridgeL0,
    tertiaryContainer = FridgeAmberL,
    onTertiaryContainer = FridgeAmber,
    error = FridgeRed,
    onError = FridgeL0,
    errorContainer = FridgeRedL,
    onErrorContainer = FridgeRed,
    background = FridgeBg,
    onBackground = FridgeInk,
    surface = FridgeL0,
    onSurface = FridgeInk,
    surfaceVariant = FridgeL1,
    onSurfaceVariant = FridgeInk2,
    outline = FridgeBorder,
    outlineVariant = FridgeBorder2,
    inverseSurface = FridgeInk,
    inverseOnSurface = FridgeL0,
    scrim = Color.Black.copy(alpha = 0.25f)
)

@Composable
fun FridgeAIAndroidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> EarthDarkColorScheme
        else -> EarthColorScheme
    }
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = FridgeBg.toArgb()
            window.navigationBarColor = FridgeL0.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
