package app.what.reservation.features.main.dependencies

import app.what.reservation.core.navigation.Registry
import app.what.reservation.core.navigation.register
import app.what.reservation.features.main.MainFeature

val mainRegistry: Registry = {
    register(::MainFeature)
}