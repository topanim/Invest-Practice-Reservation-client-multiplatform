package app.what.reservation.features.services.domain.models

import app.what.reservation.core.foundation.data.RemoteState
import app.what.reservation.domain.models.ServiceDomain

data class ServicesState(
    val services: List<ServiceDomain> = emptyList(),
    val servicesState: RemoteState = RemoteState.Idle
)