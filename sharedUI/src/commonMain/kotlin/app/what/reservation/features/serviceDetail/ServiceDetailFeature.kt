package app.what.reservation.features.serviceDetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import app.what.reservation.core.foundation.core.Feature
import app.what.reservation.core.navigation.NavComponent
import app.what.reservation.features.serviceDetail.domain.ServiceDetailController
import app.what.reservation.features.serviceDetail.domain.models.ServiceDetailEvent
import app.what.reservation.features.serviceDetail.presentation.ServiceDetailView
import app.what.reservation.utils.Screens
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

class ServiceDetailFeature(
    override val data: Screens.ServiceDetail
) : Feature<ServiceDetailController, ServiceDetailEvent>(),
    NavComponent<Screens.ServiceDetail>,
    KoinComponent {

    override val controller: ServiceDetailController by inject { parametersOf(data.id) }

    @Composable
    override fun content(modifier: Modifier) {
        val state = controller.collectStates()
        val action = controller.collectActions()

        ServiceDetailView(state, listener)

        LaunchedEffect(action.value) {
            when (action.value) {
                null -> Unit
            }
        }
    }
}