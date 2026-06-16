package app.what.reservation.data.remote.dto

import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class UserDto(
    val id: Uuid,
    val googleId: String,
    val email: String,
    val name: String,
    val picture: String? = null,
    val isBanned: Boolean = false
)