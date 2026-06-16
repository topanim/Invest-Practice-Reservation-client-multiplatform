package app.what.reservation.core.foundation.data.settings.types

import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import app.what.reservation.core.foundation.core.UIComponent
import app.what.reservation.core.foundation.data.settings.PreferenceStorage
import app.what.reservation.core.foundation.data.settings.views.BaseSettingRow

fun PreferenceStorage.Value<Boolean>.asSwitch(sideEffect: (Boolean) -> Unit = {}) =
    object : UIComponent {
        @Composable
        override fun content(modifier: Modifier) {
            val state by collect()
            BaseSettingRow(this@asSwitch, modifier) {
                Switch(checked = state ?: false, onCheckedChange = { sideEffect(it); set(it) })
            }
        }
    }