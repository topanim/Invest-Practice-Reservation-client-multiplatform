package app.what.reservation.core.foundation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import com.materialkolor.dynamicColorScheme

val LocalThemeIsDark = compositionLocalOf<Boolean> { error("LocalThemeIsDark is not provided") }

@Composable
fun WHATTheme(
    theme: ColorScheme,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {

    val colorScheme = if (dynamicColor) dynamicColorScheme(Color(0x546B41), isDarkTheme)
    else theme

    CompositionLocalProvider(
        LocalThemeIsDark provides isDarkTheme
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}