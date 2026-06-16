package app.what.reservation.features.services.domain

import androidx.lifecycle.viewModelScope
import app.what.reservation.core.foundation.core.UIController
import app.what.reservation.core.foundation.data.RemoteState
import app.what.reservation.core.foundation.services.AppLogger.Companion.Auditor
import app.what.reservation.core.foundation.utils.launchSafe
import app.what.reservation.data.local.AppValues
import app.what.reservation.data.remote.ServicesService
import app.what.reservation.domain.models.ServiceDomain
import app.what.reservation.features.services.domain.models.ServicesAction
import app.what.reservation.features.services.domain.models.ServicesAction.NavigateToServiceDetail
import app.what.reservation.features.services.domain.models.ServicesEvent
import app.what.reservation.features.services.domain.models.ServicesState
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class ServicesController(
    values: AppValues,
    private val services: ServicesService
) : UIController<ServicesState, ServicesAction, ServicesEvent>(
    ServicesState()
) {
    init {
        loadServices()
    }

    override fun obtainEvent(viewEvent: ServicesEvent) = when (viewEvent) {
        ServicesEvent.OnServicesListRefresh -> loadServices()
        is ServicesEvent.OnServiceClicked -> setAction(
            NavigateToServiceDetail(viewEvent.value.id)
        )
    }

    private fun loadServices() {
        Auditor.debug("d", "load services")
        viewModelScope.launchSafe(
            debug = true,
            onFailure = {
                updateState { copy(servicesState = RemoteState.Error(it)) }
                Auditor.err("d", it.message ?: "", it)
            }
        ) {
            val data = services.getAll().map {
                ServiceDomain(
                    it.id,
                    it.title,
                    it.description,
                    it.durationMinutes,
                    it.price,
                    it.bufferMinutes,
                    it.isActive
                )
            }

            Auditor.debug("d", data.toString())
            updateState { copy(servicesState = RemoteState.Success, services = data) }
        }
    }
}