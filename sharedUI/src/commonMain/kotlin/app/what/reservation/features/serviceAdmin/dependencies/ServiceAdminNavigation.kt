package app.what.reservation.features.serviceAdmin.dependencies

import app.what.reservation.core.navigation.Registry
import app.what.reservation.core.navigation.register
import app.what.reservation.features.serviceAdmin.ServiceAdminFeature

val serviceAdminRegistry: Registry = {
    register(::ServiceAdminFeature)
}