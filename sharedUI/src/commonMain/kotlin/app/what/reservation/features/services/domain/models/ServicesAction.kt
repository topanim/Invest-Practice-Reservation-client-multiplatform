package app.what.reservation.features.services.domain.models

import kotlin.uuid.Uuid

sealed interface ServicesAction {
    class NavigateToServiceDetail(val serviceId: Uuid) : ServicesAction
}