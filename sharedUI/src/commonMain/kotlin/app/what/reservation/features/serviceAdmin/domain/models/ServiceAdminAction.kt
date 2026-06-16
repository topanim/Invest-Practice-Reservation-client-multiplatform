package app.what.reservation.features.serviceAdmin.domain.models

import kotlin.uuid.Uuid

sealed interface ServiceAdminAction {
    class NavigateToServiceDetail(val serviceId: Uuid) : ServiceAdminAction
}