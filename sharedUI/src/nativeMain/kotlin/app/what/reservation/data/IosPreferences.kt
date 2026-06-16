package app.what.reservation.data

import app.what.reservation.core.foundation.data.settings.Preference
import platform.Foundation.NSUserDefaults

class NativePreference : Preference {

    private val prefs = NSUserDefaults.standardUserDefaults
    private val listeners = mutableListOf<(String?) -> Unit>()

    override fun registerOnPreferenceChangeListener(
        block: (key: String?) -> Unit
    ) {
        listeners += block
    }

    override fun getString(
        key: String,
        default: String?
    ): String? {
        return prefs.stringForKey(key) ?: default
    }

    override fun putString(
        key: String,
        value: String?
    ) {
        if (value == null) {
            prefs.removeObjectForKey(key)
        } else {
            prefs.setObject(value, key)
        }

        listeners.forEach { it(key) }
    }
}