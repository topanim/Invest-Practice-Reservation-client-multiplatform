package app.what.reservation.features.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import app.what.reservation.core.foundation.core.Feature
import app.what.reservation.core.foundation.services.AppLogger.Companion.Auditor
import app.what.reservation.core.navigation.NavComponent
import app.what.reservation.core.navigation.rememberNavigator
import app.what.reservation.features.auth.domain.AuthController
import app.what.reservation.features.auth.domain.models.AuthAction
import app.what.reservation.features.auth.domain.models.AuthEvent
import app.what.reservation.features.auth.presentation.AuthView
import app.what.reservation.utils.Screens
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AuthFeature(
    override val data: Screens.Auth
) : Feature<AuthController, AuthEvent>(),
    NavComponent<Screens.Auth>,
    KoinComponent {

    override val controller: AuthController by inject()

    @Composable
    override fun content(modifier: Modifier) {
        val state = controller.collectStates()
        val action = controller.collectActions()
        val navigator = rememberNavigator()

        LaunchedEffect(Unit) {
            Auditor.debug("nav", navigator.hashCode().toString())
        }

        AuthView(state, listener)

        when (action.value) {
            null -> Unit
            AuthAction.NavigateToMain -> {
                Auditor.debug("d", "navigator main")
                Auditor.debug("nav", navigator.c.currentDestination.toString())
                navigator.c.navigate(Screens.Main)
                Auditor.debug("nav", navigator.c.currentDestination.toString())
                Auditor.debug("d", "navigator main1")
                controller.clearAction()
            }
        }
    }
}