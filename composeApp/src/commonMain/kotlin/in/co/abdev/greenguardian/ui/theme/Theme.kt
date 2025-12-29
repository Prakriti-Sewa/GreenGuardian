package `in`.co.abdev.greenguardian.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Light theme colors
private val LightGreen = Color(0xFF4CAF50)
private val LightGreenVariant = Color(0xFF388E3C)
private val LightBackground = Color(0xFFF1F8E9)
private val LightSurface = Color(0xFFFFFFFF)
private val LightOnPrimary = Color(0xFFFFFFFF)
private val LightOnSecondary = Color(0xFF000000)
private val LightError = Color(0xFFB00020)

// Dark theme colors
private val DarkGreen = Color(0xFF81C784)
private val DarkGreenVariant = Color(0xFF4CAF50)
private val DarkBackground = Color(0xFF121212)
private val DarkSurface = Color(0xFF1E1E1E)
private val DarkOnPrimary = Color(0xFF000000)
private val DarkOnSecondary = Color(0xFFFFFFFF)
private val DarkError = Color(0xFFCF6679)

private val LightColorScheme = lightColorScheme(
    primary = LightGreen,
    onPrimary = LightOnPrimary,
    primaryContainer = LightGreenVariant,
    secondary = LightGreenVariant,
    onSecondary = LightOnSecondary,
    background = LightBackground,
    onBackground = Color.Black,
    surface = LightSurface,
    onSurface = Color.Black,
    error = LightError,
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkGreen,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkGreenVariant,
    secondary = DarkGreenVariant,
    onSecondary = DarkOnSecondary,
    background = DarkBackground,
    onBackground = Color.White,
    surface = DarkSurface,
    onSurface = Color.White,
    error = DarkError,
    onError = Color.Black
)

@Composable
fun GreenGuardianTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }
    
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
