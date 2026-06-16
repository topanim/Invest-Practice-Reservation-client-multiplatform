package app.what.reservation.features.serviceAdmin.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.what.reservation.core.foundation.core.Listener
import app.what.reservation.core.foundation.ui.forms.IntCounterFormField
import app.what.reservation.core.foundation.ui.forms.IntFormField
import app.what.reservation.core.foundation.ui.forms.StringFormField
import app.what.reservation.core.foundation.utils.freeze
import app.what.reservation.features.serviceAdmin.domain.models.ServiceAdminEvent
import app.what.reservation.features.serviceAdmin.domain.models.ServiceAdminState
import app.what.reservation.ui.theme.Strings


fun serviceCreateForm(
    state: State<ServiceAdminState>,
    listener: Listener<ServiceAdminEvent>
) = @Composable {

    val title = StringFormField(Strings.Default.serviceCreatingFormValueTitle).freeze()
    val description =
        StringFormField(Strings.Default.serviceCreatingFormValueDescription).freeze()
    val durationMinutes = IntCounterFormField(
        Strings.Default.serviceCreatingFormValueDurationMinutes,
        10, 10..720, 10
    ).freeze()
    val price = IntFormField(
        Strings.Default.serviceCreatingFormValuePrice, 0,
        validator = {
            if (it == null) "Введите число"
            else null
        }
    ).freeze()
    val buffer = IntCounterFormField(
        "Перерыв", 0,
        0..60, 5
    ).freeze()

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
        ) {
            Text(
                Strings.Default.serviceCreatingFormTitle,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            title.content(Modifier)
            description.content(Modifier)
            durationMinutes.content(Modifier)
            price.content(Modifier)
            buffer.content(Modifier)

            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = shapes.medium,
                onClick = {
                    listener(
                        ServiceAdminEvent.OnServiceCreateFormConfirmed(
                            title.data,
                            description.data,
                            durationMinutes.data,
                            price.data!!,
                            buffer.data
                        )
                    )
                }
            ) {
                Text(Strings.Default.serviceCreatingFormConfirm)
            }
        }
    }
}
