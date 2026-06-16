package app.what.reservation.features.serviceAdmin.domain.models

import app.what.reservation.core.foundation.data.RemoteState
import app.what.reservation.domain.models.ServiceDomain
import app.what.reservation.domain.models.UserDomain

data class ServiceAdminState(
    val services: List<ServiceDomain> = emptyList(),
    val servicesState: RemoteState = RemoteState.Idle,
    val clients: List<UserDomain> = emptyList(),
    val clientsState: RemoteState = RemoteState.Idle,
)