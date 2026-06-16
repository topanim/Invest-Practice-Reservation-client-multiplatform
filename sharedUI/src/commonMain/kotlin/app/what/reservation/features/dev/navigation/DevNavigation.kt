package app.what.reservation.features.dev.navigation

import androidx.navigation.compose.composable
import app.what.reservation.core.navigation.Registry
import app.what.reservation.features.dev.DevFeature
import app.what.reservation.utils.Screens

val devRegistry: Registry = {
    composable<Screens.Dev> { DevFeature() }
}