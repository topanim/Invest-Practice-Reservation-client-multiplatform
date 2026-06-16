package app.what.reservation.features.services

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import app.what.reservation.core.foundation.core.Feature
import app.what.reservation.core.navigation.NavComponent
import app.what.reservation.core.navigation.rememberNavigator
import app.what.reservation.features.services.domain.ServicesController
import app.what.reservation.features.services.domain.models.ServicesAction
import app.what.reservation.features.services.domain.models.ServicesEvent
import app.what.reservation.features.services.presentation.ServicesView
import app.what.reservation.utils.Screens
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ServicesFeature(
    override val data: Screens.Services
) : Feature<ServicesController, ServicesEvent>(),
    NavComponent<Screens.Services>,
    KoinComponent {

    override val controller: ServicesController by inject()

    @Composable
    override fun content(modifier: Modifier) {
        val state = controller.collectStates()
        val action = controller.collectActions()
        val rootNavigator = rememberNavigator(1)

        ServicesView(state, listener)

        LaunchedEffect(action.value) {
            action.value.let {
                when (it) {
                    null -> Unit
                    is ServicesAction.NavigateToServiceDetail ->
                        rootNavigator.c.navigate(Screens.ServiceDetail(it.serviceId.toString()))
                }

                if (it != null) controller.clearAction()
            }
        }
    }
}