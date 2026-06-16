package app.what.reservation.features.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import app.what.reservation.core.foundation.core.Feature
import app.what.reservation.core.navigation.NavComponent
import app.what.reservation.core.navigation.rememberNavigator
import app.what.reservation.features.profile.domain.ProfileController
import app.what.reservation.features.profile.domain.models.ProfileAction
import app.what.reservation.features.profile.domain.models.ProfileEvent
import app.what.reservation.features.profile.presentation.ProfileView
import app.what.reservation.utils.Screens
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ProfileFeature(
    override val data: Screens.Profile
) : Feature<ProfileController, ProfileEvent>(),
    NavComponent<Screens.Profile>,
    KoinComponent {

    override val controller: ProfileController by inject()

    @Composable
    override fun content(modifier: Modifier) {
        val state = controller.collectStates()
        val action = controller.collectActions()
        val navigator = rememberNavigator(1)

        LaunchedEffect(Unit) {
            listener(ProfileEvent.OnOpen)
        }

        ProfileView(state, listener)

        LaunchedEffect(action.value) {
            when (action.value) {
                null -> Unit
                ProfileAction.NavigateToAuth -> navigator.c.navigate(Screens.Auth)
            }

            controller.clearAction()
        }
    }
}