package app.what.reservation.features.services.domain.models

import app.what.reservation.domain.models.ServiceDomain

sealed interface ServicesEvent {
    object OnServicesListRefresh : ServicesEvent
    class OnServiceClicked(val value: ServiceDomain) : ServicesEvent
}