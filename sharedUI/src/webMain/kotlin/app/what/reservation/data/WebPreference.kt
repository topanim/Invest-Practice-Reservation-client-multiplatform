package app.what.reservation.data

import app.what.reservation.core.foundation.data.settings.Preference
import kotlinx.browser.window

class WebPreference : Preference {

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
        return window.localStorage.getItem(key) ?: default
    }

    override fun putString(
        key: String,
        value: String?
    ) {
        if (value == null) {
            window.localStorage.removeItem(key)
        } else {
            window.localStorage.setItem(key, value)
        }

        listeners.forEach { it(key) }
    }
}