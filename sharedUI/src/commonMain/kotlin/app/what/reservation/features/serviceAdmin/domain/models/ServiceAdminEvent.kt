package app.what.reservation.features.serviceAdmin.domain.models

import app.what.reservation.domain.models.ServiceDomain

sealed interface ServiceAdminEvent {
    object OnServicesListRefresh : ServiceAdminEvent
    class OnServiceClicked(val value: ServiceDomain) : ServiceAdminEvent
    class OnServiceCreateFormConfirmed(
        val title: String,
        val description: String,
        val durationMinutes: Int,
        val price: Int,
        val bufferMinutes: Int
    ) : ServiceAdminEvent
}