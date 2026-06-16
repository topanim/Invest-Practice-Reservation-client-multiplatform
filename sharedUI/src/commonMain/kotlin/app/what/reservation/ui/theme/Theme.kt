package app.what.reservation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import app.what.reservation.core.foundation.ui.theme.WHATTheme
import app.what.reservation.data.local.AppValues
import app.what.reservation.data.local.ThemeStyle
import app.what.reservation.data.local.ThemeType
import app.what.reservation.utils.rememberAppValues
import com.materialkolor.ktx.DynamicScheme
import com.materialkolor.toColorScheme

@Composable
fun AppTheme(
    settings: AppValues = rememberAppValues(),
    content: @Composable () -> Unit
) {
    val themeType by settings.themeType.collect()
    val themeStyle by settings.themeStyle.collect()
    val themeColor by settings.themeColor.collect()

    val isDarkTheme = when (themeType) {
        ThemeType.Dark -> true
        ThemeType.System -> isSystemInDarkTheme()
        else -> false
    }

    val theme = when (themeStyle) {
        ThemeStyle.CustomColor -> DynamicScheme(Color(themeColor!!), isDarkTheme)
        else -> DynamicScheme(Color(0xFF94FF28), isDarkTheme)
    }.toColorScheme()

    WHATTheme(
        theme = theme,
        dynamicColor = themeStyle == ThemeStyle.Material,
        isDarkTheme = isDarkTheme,
        content = content
    )
}