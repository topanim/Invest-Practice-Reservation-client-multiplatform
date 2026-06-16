package app.what.reservation.features.auth.dependencies

import app.what.reservation.core.navigation.Registry
import app.what.reservation.core.navigation.register
import app.what.reservation.features.auth.AuthFeature

val authRegistry: Registry = {
    register(::AuthFeature)
}