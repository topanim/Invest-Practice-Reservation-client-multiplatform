package app.what.reservation.core.foundation.data.settings.types

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.what.reservation.core.foundation.core.UIComponent

fun customSetting(block: @Composable (Modifier) -> Unit) = object : UIComponent {
    @Composable
    override fun content(modifier: Modifier) = block(modifier)
}