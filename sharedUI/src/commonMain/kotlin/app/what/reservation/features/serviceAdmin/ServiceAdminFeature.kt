package app.what.reservation.features.serviceAdmin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import app.what.reservation.core.foundation.core.Feature
import app.what.reservation.core.navigation.NavComponent
import app.what.reservation.core.navigation.rememberNavigator
import app.what.reservation.features.serviceAdmin.domain.ServiceAdminController
import app.what.reservation.features.serviceAdmin.domain.models.ServiceAdminAction
import app.what.reservation.features.serviceAdmin.domain.models.ServiceAdminEvent
import app.what.reservation.features.serviceAdmin.presentation.ServiceAdminView
import app.what.reservation.utils.Screens
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ServiceAdminFeature(
    override val data: Screens.ServiceAdmin
) : Feature<ServiceAdminController, ServiceAdminEvent>(),
    NavComponent<Screens.ServiceAdmin>,
    KoinComponent {

    override val controller: ServiceAdminController by inject()

    @Composable
    override fun content(modifier: Modifier) {
        val state = controller.collectStates()
        val action = controller.collectActions()
        val rootNavigator = rememberNavigator(1)

        ServiceAdminView(state, listener)

        LaunchedEffect(action.value) {
            when (val action = action.value) {
                null -> Unit
                is ServiceAdminAction.NavigateToServiceDetail -> rootNavigator.c.navigate(
                    Screens.ServiceDetail(action.serviceId.toString())
                )
            }

            controller.clearAction()
        }
    }
}