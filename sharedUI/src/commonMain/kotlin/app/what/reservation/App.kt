package app.what.reservation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import app.what.reservation.core.foundation.services.AppLogger.Companion.Auditor
import app.what.reservation.core.navigation.NavigationHost
import app.what.reservation.core.navigation.ProvideGlobalDialog
import app.what.reservation.core.navigation.ProvideGlobalSheet
import app.what.reservation.core.navigation.rememberHostNavigator
import app.what.reservation.data.local.AppValues
import app.what.reservation.data.local.AuthSessionManager
import app.what.reservation.features.auth.dependencies.authRegistry
import app.what.reservation.features.main.dependencies.mainRegistry
import app.what.reservation.features.serviceDetail.dependencies.serviceDetailRegistry
import app.what.reservation.ui.theme.AppTheme
import app.what.reservation.utils.ProvideGLobalAppValues
import app.what.reservation.utils.Screens
import org.koin.compose.koinInject

@Preview
@Composable
fun App() {
    val appValues = koinInject<AppValues>()
    val authSessionManager = koinInject<AuthSessionManager>()
    val start =
        remember { if (authSessionManager.getAccessTokenOrNull() == null) Screens.Auth else Screens.Main }
    val navigator = rememberHostNavigator()

    LaunchedEffect(Unit) {
        navigator.c.addOnDestinationChangedListener { controller, destination, state ->
            Auditor.debug("nav", "destination: $destination")
        }

    }

    ProvideGLobalAppValues(appValues) {
        AppTheme {
            ProvideGlobalDialog {
                ProvideGlobalSheet {
                    NavigationHost(
                        navigator = navigator,
                        modifier = Modifier.fillMaxSize().background(colorScheme.background),
                        start = start
                    ) {
                        authRegistry()
                        mainRegistry()
                        serviceDetailRegistry()
                    }
                }
            }
        }
    }
}