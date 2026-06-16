package app.what.reservation.data.remote.dto

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class AuthCredentials(
    val accessToken: String,
    val refreshToken: RefreshToken,
    val expiresAt: LocalDateTime
)

@Serializable
data class RefreshToken(
    val userId: Uuid,
    val id: Uuid,
    val createdAt: LocalDateTime,
    val expiresAt: LocalDateTime,
)