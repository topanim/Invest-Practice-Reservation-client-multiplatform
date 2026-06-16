package app.what.reservation.features.services.dependencies

import app.what.reservation.core.navigation.Registry
import app.what.reservation.core.navigation.register
import app.what.reservation.features.services.ServicesFeature

val servicesRegistry: Registry = {
    register(::ServicesFeature)
}