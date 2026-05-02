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

private val DarkColorScheme = darkColorScheme(
    primary = IceBlue60,
    onPrimary = Color.White,
    primaryContainer = IceBlue20,
    onPrimaryContainer = IceBlue80,
    
    secondary = FreshGreen60,
    onSecondary = Color.White,
    secondaryContainer = FreshGreen20,
    onSecondaryContainer = FreshGreen80,
    
    tertiary = WarningOrange60,
    onTertiary = Color.White,
    tertiaryContainer = WarningOrange20,
    onTertiaryContainer = WarningOrange80,
    
    error = ErrorRed60,
    onError = Color.White,
    errorContainer = ErrorRed20,
    onErrorContainer = ErrorRed80,
    
    background = Color(0xFF1C1C1E),
    onBackground = Color(0xFFE5E5E7),
    surface = Color(0xFF2C2C2E),
    onSurface = Color(0xFFE5E5E7),
    surfaceVariant = Color(0xFF3A3A3C),
    onSurfaceVariant = Grey60
)

private val LightColorScheme = lightColorScheme(
    primary = IceBlue40,
    onPrimary = Color.White,
    primaryContainer = IceBlue80,
    onPrimaryContainer = IceBlue20,
    
    secondary = FreshGreen40,
    onSecondary = Color.White,
    secondaryContainer = FreshGreen80,
    onSecondaryContainer = FreshGreen20,
    
    tertiary = WarningOrange40,
    onTertiary = Color.White,
    tertiaryContainer = WarningOrange80,
    onTertiaryContainer = WarningOrange20,
    
    error = ErrorRed40,
    onError = Color.White,
    errorContainer = ErrorRed80,
    onErrorContainer = ErrorRed20,
    
    background = Color(0xFFFAFAFA),
    onBackground = Color(0xFF1C1C1E),
    surface = Color.White,
    onSurface = Color(0xFF1C1C1E),
    surfaceVariant = Grey90,
    onSurfaceVariant = Grey40
)

@Composable
fun FridgeAIAndroidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // 关闭动态颜色以使用自定义主题
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
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}