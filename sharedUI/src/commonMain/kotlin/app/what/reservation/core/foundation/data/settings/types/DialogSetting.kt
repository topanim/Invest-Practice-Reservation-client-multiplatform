package app.what.reservation.core.foundation.data.settings.types

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.what.reservation.core.foundation.core.UIComponent
import app.what.reservation.core.foundation.data.settings.PreferenceStorage
import app.what.reservation.core.foundation.data.settings.views.BaseSettingRow
import app.what.reservation.core.foundation.ui.controllers.rememberDialogController

fun <T : Any> PreferenceStorage.Value<T>.asDialog(
    onContent: @Composable (value: PreferenceStorage.Value<T>, setValue: (T?) -> Unit) -> Unit
) = object : UIComponent {
    @Composable
    override fun content(modifier: Modifier) {
        val dialog = rememberDialogController()

        BaseSettingRow(
            value = this@asDialog,
            modifier = modifier,
            onClick = { dialog.open { onContent(this@asDialog, this@asDialog::set) } }
        ) {
//            Icon(Icons.AutoMirrored.Rounded.KeyboardArrowRight, null, tint = colorScheme.outline)
        }
    }
}