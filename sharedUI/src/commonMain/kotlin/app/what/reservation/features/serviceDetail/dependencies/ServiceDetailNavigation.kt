package app.what.reservation.features.serviceDetail.dependencies

import app.what.reservation.core.navigation.Registry
import app.what.reservation.core.navigation.register
import app.what.reservation.features.serviceDetail.ServiceDetailFeature

val serviceDetailRegistry: Registry = {
    register(::ServiceDetailFeature)
}