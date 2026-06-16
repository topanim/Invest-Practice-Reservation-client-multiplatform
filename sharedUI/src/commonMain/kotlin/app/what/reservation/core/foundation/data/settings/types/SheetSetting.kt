package app.what.reservation.core.foundation.data.settings.types

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.what.reservation.core.foundation.core.UIComponent
import app.what.reservation.core.foundation.data.settings.PreferenceStorage
import app.what.reservation.core.foundation.data.settings.views.BaseSettingRow
import app.what.reservation.core.foundation.ui.controllers.rememberSheetController

fun <T : Any> PreferenceStorage.Value<T>.asSheet(
    onContent: @Composable (value: PreferenceStorage.Value<T>, setValue: (T?) -> Unit) -> Unit
) = object : UIComponent {
    @Composable
    override fun content(modifier: Modifier) {
        val sheet = rememberSheetController()

        BaseSettingRow(
            value = this@asSheet,
            modifier = modifier,
            onClick = {
                sheet.open { onContent(this@asSheet, this@asSheet::set) }
            }
        ) {
//            Icon(
//                imageVector = ,
//                contentDescription = null,
//                tint = colorScheme.outline,
//                modifier = Modifier.size(20.dp)
//            )
        }
    }
}