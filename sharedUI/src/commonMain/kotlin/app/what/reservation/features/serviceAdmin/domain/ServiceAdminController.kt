package app.what.reservation.features.serviceAdmin.domain

import androidx.lifecycle.viewModelScope
import app.what.reservation.core.foundation.core.UIController
import app.what.reservation.core.foundation.data.RemoteState
import app.what.reservation.core.foundation.services.AppLogger.Companion.Auditor
import app.what.reservation.core.foundation.utils.launchSafe
import app.what.reservation.data.local.AppValues
import app.what.reservation.data.remote.ServicesService
import app.what.reservation.data.remote.UsersService
import app.what.reservation.data.remote.dto.CreateServiceRequest
import app.what.reservation.domain.models.ServiceDomain
import app.what.reservation.domain.models.UserDomain
import app.what.reservation.features.serviceAdmin.domain.models.ServiceAdminAction
import app.what.reservation.features.serviceAdmin.domain.models.ServiceAdminAction.NavigateToServiceDetail
import app.what.reservation.features.serviceAdmin.domain.models.ServiceAdminEvent
import app.what.reservation.features.serviceAdmin.domain.models.ServiceAdminState

class ServiceAdminController(
    values: AppValues,
    private val services: ServicesService,
    private val users: UsersService,
) : UIController<ServiceAdminState, ServiceAdminAction, ServiceAdminEvent>(
    ServiceAdminState()
) {
    init {
        load()
    }

    override fun obtainEvent(viewEvent: ServiceAdminEvent) = when (viewEvent) {
        is ServiceAdminEvent.OnServiceClicked -> setAction(NavigateToServiceDetail(viewEvent.value.id))
        ServiceAdminEvent.OnServicesListRefresh -> load()
        is ServiceAdminEvent.OnServiceCreateFormConfirmed -> createService(viewEvent)
    }

    private fun load() {
        loadServices()
        loadClients()
    }

    private fun createService(event: ServiceAdminEvent.OnServiceCreateFormConfirmed) {
        viewModelScope.launchSafe(
            onFailure = {
                Auditor.err("d", it.message ?: "", it)
            }
        ) {
            services.create(
                CreateServiceRequest(
                    event.title,
                    event.description,
                    event.durationMinutes,
                    event.price.toFloat(),
                    event.bufferMinutes
                )
            )

            loadServices()
        }
    }

    private fun loadClients() {
        Auditor.debug("d", "load services")
        viewModelScope.launchSafe(
            debug = true,
            onFailure = {
                updateState { copy(clientsState = RemoteState.Error(it)) }
                Auditor.err("d", it.message ?: "", it)
            }
        ) {
            updateState { copy(clientsState = RemoteState.Loading) }
            val data = users.clients().map {
                UserDomain(
                    it.id,
                    it.name,
                    it.email,
                    it.picture,
                )
            }

            Auditor.debug("d", data.toString())
            updateState { copy(clientsState = RemoteState.Success, clients = data) }
        }
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
            val data = services.my().map {
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