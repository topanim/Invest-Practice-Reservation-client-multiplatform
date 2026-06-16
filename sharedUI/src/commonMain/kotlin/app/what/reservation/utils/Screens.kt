package app.what.reservation.utils

import app.what.reservation.core.navigation.NavProvider
import kotlinx.serialization.Serializable

sealed class Screens : NavProvider() {
    @Serializable
    object Auth : Screens()

    @Serializable
    object Main : Screens()

    @Serializable
    object Profile : Screens()

    @Serializable
    object Services : Screens()

    @Serializable
    data class ServiceDetail(
        val id: String
    ) : Screens()

    @Serializable
    object Dev : Screens()

    @Serializable
    object ServiceAdmin : Screens()
}