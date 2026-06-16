package app.what.reservation.data

import app.what.reservation.core.foundation.data.settings.Preference
import java.util.prefs.Preferences

class JvmPreference : Preference {
    private val prefs =
        Preferences.userRoot().node("app_preferences")

    private val listeners =
        mutableListOf<(String?) -> Unit>()

    override fun registerOnPreferenceChangeListener(
        block: (key: String?) -> Unit
    ) {
        listeners += block
    }

    override fun getString(
        key: String,
        default: String?
    ): String? {
        return prefs.get(key, default)
    }

    override fun putString(
        key: String,
        value: String?
    ) {
        if (value == null) {
            prefs.remove(key)
        } else {
            prefs.put(key, value)
        }

        listeners.forEach { it(key) }
    }
}