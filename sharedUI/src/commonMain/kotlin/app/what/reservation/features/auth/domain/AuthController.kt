package app.what.reservation.features.auth.domain

import androidx.lifecycle.viewModelScope
import app.what.reservation.core.foundation.core.UIController
import app.what.reservation.core.foundation.services.AppLogger.Companion.Auditor
import app.what.reservation.core.foundation.utils.launchSafe
import app.what.reservation.data.local.AuthSessionManager
import app.what.reservation.data.remote.AuthService
import app.what.reservation.features.auth.domain.models.AuthAction
import app.what.reservation.features.auth.domain.models.AuthEvent
import app.what.reservation.features.auth.domain.models.AuthState
import app.what.reservation.infrastructure.auth.BrowserLauncher
import app.what.reservation.infrastructure.auth.OAuthCallbackServer

class AuthController(
    private val authService: AuthService,
    private val authSessionManager: AuthSessionManager,
    private val browserLauncher: BrowserLauncher,
    private val oAuthCallbackServer: OAuthCallbackServer
) : UIController<AuthState, AuthAction, AuthEvent>(
    AuthState()
) {
    override fun obtainEvent(viewEvent: AuthEvent) = when (viewEvent) {
        AuthEvent.OnSignWithGoogleClicked -> signWithGoogle()
    }

    private fun signWithGoogle() {
        viewModelScope.launchSafe(
            debug = true,
            onFinally = {
                oAuthCallbackServer.stop()
                Auditor.debug("d", "stop")
            },
        ) {
            println("------------------------")
            oAuthCallbackServer.start()
            Auditor.debug("d", "server started")
            val authUrl = authService.getGoogleAuthUrl(oAuthCallbackServer.callbackUrl)!!
            Auditor.debug("d", "get atuhUrl: $authUrl")
            browserLauncher.open(authUrl)

            val sessionId = oAuthCallbackServer.awaitSessionId()
            Auditor.debug("d", "get sessionId: $sessionId")
            val credentials = authService.exchange(sessionId)
            Auditor.debug("d", "get credentials: $credentials")

            authSessionManager.setCredentials(credentials)
            Auditor.debug("d", "save credentials")

            setAction(AuthAction.NavigateToMain)
            Auditor.debug("d", "navigate main")
        }
    }
}