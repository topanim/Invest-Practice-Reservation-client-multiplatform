package app.what.reservation.data.local

import app.what.reservation.core.foundation.data.settings.Preference
import app.what.reservation.core.foundation.data.settings.PreferenceStorage
import kotlinx.serialization.builtins.serializer
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AuthStorage(prefs: Preference) : PreferenceStorage(prefs) {
    @OptIn(ExperimentalUuidApi::class)
    val userId = createValue("user_id", Uuid.NIL, Uuid.serializer())
    val accessToken = createValue("access_token", null, String.serializer())

    @OptIn(ExperimentalUuidApi::class)
    val refreshToken = createValue("refresh_token", null, Uuid.serializer())
}