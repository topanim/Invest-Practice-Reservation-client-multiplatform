package app.what.reservation.features.serviceDetail.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.what.reservation.core.foundation.core.Listener
import app.what.reservation.core.foundation.ui.Gap
import app.what.reservation.core.foundation.ui.forms.DateRangeFormField
import app.what.reservation.core.foundation.ui.forms.DateTimeRangeFormField
import app.what.reservation.core.foundation.ui.forms.DaysOfWeekFormField
import app.what.reservation.core.foundation.ui.forms.LocalTimeFormField
import app.what.reservation.core.foundation.ui.forms.StringFormField
import app.what.reservation.core.foundation.utils.freeze
import app.what.reservation.features.serviceDetail.domain.models.ServiceDetailEvent
import app.what.reservation.features.serviceDetail.domain.models.ServiceDetailState
import app.what.reservation.ui.theme.Strings
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days

fun serviceAddAvailabilityRuleForm(
    state: State<ServiceDetailState>,
    listener: Listener<ServiceDetailEvent>
) = @Composable {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        LaunchedEffect(Unit) {
            listener(ServiceDetailEvent.OnAddAdminFormOpen)
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(max = 500.dp)
                .padding(12.dp, 8.dp)
                .padding(top = 12.dp)
        ) {
            val days = DaysOfWeekFormField(
                name = "Дни недели",
                validator = {
                    if (it.isEmpty()) "Выберите хотя бы один день"
                    else null
                }
            ).freeze()

            val startTime = LocalTimeFormField(
                name = "Время начала",
            ).freeze()

            val endTime = LocalTimeFormField(
                name = "Время начала",
                default = LocalTime(18, 0),
                validator = {
                    if (it <= startTime.data)
                        "Время окончания должно быть позже начала"
                    else null
                }
            ).freeze()

            days.content(Modifier)
            startTime.content(Modifier)
            endTime.content(Modifier)

            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = shapes.medium,
                onClick = {
                    listener(
                        ServiceDetailEvent.OnAddAvailabilityRuleFormConfirmed(
                            days.data,
                            startTime.data,
                            endTime.data
                        )
                    )
                }
            ) {
                Text(Strings.Default.add)
            }
        }
    }
}


fun serviceAddOneTimeAvailabilityRuleForm(
    state: State<ServiceDetailState>,
    listener: Listener<ServiceDetailEvent>
) = @Composable {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        LaunchedEffect(Unit) {
            listener(ServiceDetailEvent.OnAddAdminFormOpen)
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(max = 500.dp)
                .padding(12.dp, 8.dp)
                .padding(top = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            val period = DateRangeFormField(
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
                Clock.System.now().plus(2.days)
                    .toLocalDateTime(TimeZone.currentSystemDefault()).date,
            ).freeze()

            val startTime = LocalTimeFormField(
                name = "Время начала",
            ).freeze()

            val endTime = LocalTimeFormField(
                name = "Время окончания",
                default = LocalTime(18, 0),
                validator = {
                    if (it <= startTime.data)
                        "Время окончания должно быть позже начала"
                    else null
                }
            ).freeze()

            period.content(Modifier)
            startTime.content(Modifier)
            endTime.content(Modifier)

            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = shapes.medium,
                onClick = {
                    listener(
                        ServiceDetailEvent.OnAddOneTimeAvailabilityRuleFormConfirmed(
                            period.data.start,
                            period.data.endInclusive,
                            startTime.data,
                            endTime.data
                        )
                    )
                }
            ) {
                Text(Strings.Default.add)
            }
        }
    }
}


fun serviceAddAvailabilityBlockForm(
    state: State<ServiceDetailState>,
    listener: Listener<ServiceDetailEvent>
) = @Composable {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        LaunchedEffect(Unit) {
            listener(ServiceDetailEvent.OnAddAdminFormOpen)
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(max = 500.dp)
                .padding(12.dp, 8.dp)
                .padding(top = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            val period = DateTimeRangeFormField(
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
                LocalTime(0, 0),
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
                LocalTime(0, 0),
            ).freeze()

            val reason = StringFormField("Причина").freeze()

            period.content(Modifier)
            Gap(8)
            reason.content(Modifier)

            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = shapes.medium,
                onClick = {
                    listener(
                        ServiceDetailEvent.OnAddAvailabilityBlockFormConfirmed(
                            period.data.start,
                            period.data.endInclusive,
                            reason.data
                        )
                    )
                }
            ) {
                Text(Strings.Default.add)
            }
        }
    }
}
