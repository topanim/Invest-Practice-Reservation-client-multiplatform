package app.what.reservation.features.serviceDetail.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.what.reservation.core.foundation.core.Listener
import app.what.reservation.core.foundation.ui.forms.StringFormField
import app.what.reservation.core.foundation.utils.freeze
import app.what.reservation.features.serviceDetail.domain.models.ServiceDetailEvent
import app.what.reservation.features.serviceDetail.domain.models.ServiceDetailState
import kotlin.uuid.Uuid

fun bookingAdminCancelForm(
    bookingId: Uuid,
    state: State<ServiceDetailState>,
    listener: Listener<ServiceDetailEvent>
) = @Composable {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .widthIn(max = 800.dp)
                .padding(12.dp, 8.dp)
                .padding(top = 12.dp)
        ) {
            val reason = StringFormField("Причина").freeze()
            reason.content(Modifier)

            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = shapes.medium,
                onClick = {
                    listener(ServiceDetailEvent.OnCancelBookingAsAdmin(bookingId, reason.data))
                }
            ) {
                Text("Отменить")
            }
        }
    }
}
