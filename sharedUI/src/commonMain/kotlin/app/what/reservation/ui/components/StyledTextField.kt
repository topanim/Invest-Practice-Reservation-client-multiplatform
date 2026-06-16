package app.what.reservation.ui.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontFamily
import app.what.reservation.core.foundation.ui.click
import app.what.reservation.core.foundation.ui.useState
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun StyledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    debounce: Long = 0,
    placeholder: String? = null,
    shape: Shape = CircleShape,
    filter: (String) -> Boolean = { true },
    options: KeyboardOptions = KeyboardOptions.Default,
    disabled: Boolean = false,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
) {
    val (text, setText) = useState(value)
    var enabled by useState(!disabled)

    LaunchedEffect(text) {
        delay(debounce.milliseconds)
        if (text != value) onValueChange(text)
    }

    OutlinedTextField(
        enabled = enabled,
        modifier = modifier
            .clip(shape)
            .click(!enabled) { enabled = true },
        value = text,
        onValueChange = {
            if (filter(it)) setText(it)
        },
        placeholder = placeholder?.let {
            {
                Text(
                    it,
                    style = typography.bodySmall,
                    color = colorScheme.secondary
                )
            }
        },
        keyboardOptions = options,
        leadingIcon = leading,
        trailingIcon = trailing,
        singleLine = true,
        shape = shape,
        textStyle = typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = colorScheme.surfaceContainer,
            unfocusedContainerColor = colorScheme.surfaceContainer,
            focusedIndicatorColor = colorScheme.primary,
            unfocusedIndicatorColor = colorScheme.outlineVariant
        ),
    )
}