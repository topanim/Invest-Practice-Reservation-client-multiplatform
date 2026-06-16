package app.what.reservation.features.serviceDetail.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.what.reservation.core.foundation.core.Listener
import app.what.reservation.core.foundation.ui.forms.DateFormField
import app.what.reservation.core.foundation.ui.useState
import app.what.reservation.core.foundation.utils.freeze
import app.what.reservation.features.serviceDetail.domain.models.ServiceDetailEvent
import app.what.reservation.features.serviceDetail.domain.models.ServiceDetailState
import app.what.reservation.features.serviceDetail.domain.models.Slot
import app.what.reservation.ui.theme.Strings
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock


fun serviceBookingForm(
    state: State<ServiceDetailState>,
    listener: Listener<ServiceDetailEvent>
) = @Composable {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(max = 800.dp)
                .padding(12.dp, 8.dp)
                .padding(top = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            val dateInput = DateFormField(
                "Дата",
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            ).freeze()
            val date = dateInput.state()
            LaunchedEffect(date.value) {
                listener(ServiceDetailEvent.OnBookingDateChoiceComplete(date.value))
            }
            dateInput.content(Modifier)
            val selectedSlot = useState<Slot?>(null)

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                state.value.slots.forEach { slot ->
                    SlotChip(
                        from = slot.from.time,
                        to = slot.to.time,
                        selected = selectedSlot.value == slot,
                        onClick = { selectedSlot.value = slot }
                    )
                }
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = shapes.medium,
                enabled = selectedSlot.value != null,
                onClick = {
                    val s = selectedSlot.value ?: return@Button
                    listener(ServiceDetailEvent.OnBookingFormConfirmed(s))
                }
            ) {
                Text(Strings.Default.add)
            }
        }
    }
}

@Composable
private fun SlotChip(
    from: LocalTime,
    to: LocalTime,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    Surface(
        onClick = onClick,
        enabled = enabled,
        shape = shapes.medium,
        color = when {
            selected -> colorScheme.primaryContainer
            else -> colorScheme.secondaryContainer
        },
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 12.dp
            )
        ) {
            Text(
                text = from.formatTime(),
                color = if (selected)
                    colorScheme.onPrimaryContainer
                else
                    colorScheme.onSecondaryContainer,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = " – ",
                color = if (selected)
                    colorScheme.onPrimaryContainer
                else
                    colorScheme.onSecondaryContainer
            )

            Text(
                text = to.formatTime(),
                color = if (selected)
                    colorScheme.onPrimaryContainer
                else
                    colorScheme.onSecondaryContainer,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}