package app.what.reservation.data

import android.content.SharedPreferences
import androidx.core.content.edit
import app.what.reservation.core.foundation.data.settings.Preference

class AndroidPreference(private val prefs: SharedPreferences): Preference {
    override fun registerOnPreferenceChangeListener(block: (key: String?) -> Unit) {
        prefs.registerOnSharedPreferenceChangeListener { _, string ->
            block(string)
        }
    }

    override fun getString(key: String, default: String?): String? {
        return prefs.getString(key, default)
    }

    override fun putString(key: String, value: String?) {
        prefs.edit {
            putString(key, value)
            apply()
        }
    }
}