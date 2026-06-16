package app.what.reservation.features.profile.dependencies

import app.what.reservation.core.navigation.Registry
import app.what.reservation.core.navigation.register
import app.what.reservation.features.profile.ProfileFeature

val profileRegistry: Registry = {
    register(::ProfileFeature)
}