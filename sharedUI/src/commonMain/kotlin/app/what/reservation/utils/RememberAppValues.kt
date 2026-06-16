package app.what.reservation.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import app.what.reservation.data.local.AppValues

@Composable
fun ProvideGLobalAppValues(appValues: AppValues, content: @Composable () -> Unit) =
    CompositionLocalProvider(
        LocalAppValues provides appValues,
        content = content
    )


private val LocalAppValues = staticCompositionLocalOf<AppValues> {
    error("AppValues не предоставлен")
}

@Composable
fun rememberAppValues() = LocalAppValues.current