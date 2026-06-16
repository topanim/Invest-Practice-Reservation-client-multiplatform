package app.what.reservation.features.serviceDetail.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.what.reservation.core.foundation.core.Listener
import app.what.reservation.core.foundation.ui.Gap
import app.what.reservation.core.foundation.ui.SegmentTab
import app.what.reservation.core.foundation.ui.click
import app.what.reservation.core.foundation.ui.controllers.rememberDialogController
import app.what.reservation.core.foundation.ui.useState
import app.what.reservation.core.foundation.utils.freeze
import app.what.reservation.domain.models.AvailabilityBlock
import app.what.reservation.domain.models.AvailabilityRule
import app.what.reservation.domain.models.BookingDomain
import app.what.reservation.domain.models.BookingStatus
import app.what.reservation.domain.models.OneTimeAvailabilityRule
import app.what.reservation.domain.models.UserDomain
import app.what.reservation.features.serviceDetail.domain.models.ServiceDetailEvent
import app.what.reservation.features.serviceDetail.domain.models.ServiceDetailState
import app.what.reservation.ui.components.AsyncImageWithFallback
import app.what.reservation.ui.theme.Strings
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

private enum class EditPages {
    Availability,
    Bookings
}

fun serviceEditingPane(
    state: State<ServiceDetailState>,
    listener: Listener<ServiceDetailEvent>
) = @Composable {
    val dialog = rememberDialogController()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp, 8.dp)
                .padding(top = 12.dp)
        ) {
            val pagerState =
                rememberPagerState(EditPages.Availability.ordinal) { EditPages.entries.size }

            SingleChoiceSegmentedButtonRow(
                space = (-4).dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                EditPages.entries.forEachIndexed { i, it ->
                    SegmentTab(
                        i, EditPages.entries.size,
                        selected = pagerState.currentPage == it.ordinal,
                        icon = null,
                        when (it) {
                            EditPages.Availability -> "Права"
                            EditPages.Bookings -> "Брони"
                        }
                    ) {
                        scope.launch { pagerState.animateScrollToPage(it.ordinal) }
                    }
                }
            }

            Gap(8.dp)

            HorizontalPager(
                pagerState,
                verticalAlignment = Alignment.Top
            ) {
                when (it) {
                    EditPages.Availability.ordinal -> RulesSection(state, listener)
                    EditPages.Bookings.ordinal -> BookingsSection(state, listener)
                    else -> Unit
                }
            }
        }
    }
}

@Composable
private fun BookingsSection(
    state: State<ServiceDetailState>,
    listener: Listener<ServiceDetailEvent>
) = Column {
    val dialog = rememberDialogController()
    val (date, setDate) = useState<LocalDate?>(null)
    val (user, setUser) = useState<UserDomain?>(null)
    Text("Пользователь")
    Gap(8)
    NullableDropdownField(
        user,
        state.value.userCacheInfo,
        setUser,
        itemText = { it.name },
        leading = { AsyncImageWithFallback(it.avatar, Modifier.size(32.dp)) }
    )

    Gap(12)

    Text("Дата")
    Gap(8)
    NullableDropdownField(
        date,
        state.value.allReservations.map { it.startsAt.date }.toSet(),
        setDate
    )

    BookingsSection("Брони") {
        state.value.allReservations.filter {
            (date == null || it.startsAt.date == date) && (user == null || it.userId == user.id)
        }.forEach { booking ->
            BookingCard(
                booking = booking,
                users = state.value.userCacheInfo,
                onClick = {

                },
                onCancel = {
                    dialog.open(
                        content = bookingAdminCancelForm(
                            booking.id,
                            state,
                            listener
                        )
                    )
                }
            )

            Gap(8)
        }
    }
}

@Composable
private fun RulesSection(
    state: State<ServiceDetailState>,
    listener: Listener<ServiceDetailEvent>
) = Column {
    val dialog = rememberDialogController()

    val addAdminForm = serviceAddAdminForm(state, listener).freeze()
    val addAvailabilityRuleForm = serviceAddAvailabilityRuleForm(state, listener).freeze()
    val addAvailabilityOneTimeForm = serviceAddOneTimeAvailabilityRuleForm(state, listener).freeze()
    val addAvailabilityBlockForm = serviceAddAvailabilityBlockForm(state, listener).freeze()

    AdminsSection(
        admins = state.value.admins,
        onAdd = {
            dialog.open(true, content = addAdminForm)
        },
        onDelete = {
            listener(ServiceDetailEvent.OnRemoveAdminClicked(it.id))
        }
    )

    RulesSection(
        title = Strings.Default.availabilityRules,
        onAdd = {
            dialog.open(true, content = addAvailabilityRuleForm)
        }
    ) {
        state.value.availabilityRules.forEach {
            AvailabilityRuleCard(
                rule = it,
                onDelete = { listener(ServiceDetailEvent.OnRemoveAvailabilityRuleClicked(it.id)) }
            )
        }
    }

    RulesSection(
        title = "Исключения",
        onAdd = {
            dialog.open(true, content = addAvailabilityOneTimeForm)
        }
    ) {
        state.value.availabilityOneTimeRules.forEach {
            OneTimeRuleCard(
                rule = it,
                onDelete = {
                    listener(
                        ServiceDetailEvent.OnRemoveAvailabilityOneTimeRuleClicked(it.id)
                    )
                }
            )
        }
    }

    RulesSection(
        title = "Не рабочее время",
        onAdd = {
            dialog.open(true, content = addAvailabilityBlockForm)
        }
    ) {
        state.value.availabilityBlockRules.forEach {
            AvailabilityBlockCard(
                block = it,
                onDelete = { listener(ServiceDetailEvent.OnRemoveAvailabilityBlockClicked(it.id)) }
            )
        }
    }
}

@Composable
fun ColumnScope.BookingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    SectionTitle(title)

    Gap(8)

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        content()
    }
}

@Composable
private fun ColumnScope.RulesSection(
    title: String,
    onAdd: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    SectionTitle(title)

    Gap(8)

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        content()
    }

    Button(
        colors = ButtonDefaults.filledTonalButtonColors(),
        onClick = onAdd
    ) {
        Text(Strings.Default.add)
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        title,
        color = colorScheme.secondary,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium
    )

}

@Composable
private fun ColumnScope.AdminsSection(
    admins: List<UserDomain>,
    onAdd: () -> Unit,
    onDelete: (UserDomain) -> Unit
) {
    SectionTitle(Strings.Default.admins)

    Gap(8)

    FlowRow(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        itemVerticalAlignment = Alignment.CenterVertically
    ) {
        admins.forEach { admin ->
            AdminChip(admin) { onDelete(admin) }
            Gap(8)
        }

        AddCircleButton(onAdd)
    }
}

@Composable
private fun AdminChip(
    admin: UserDomain,
    onDelete: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(colorScheme.tertiaryContainer)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 4.dp, top = 4.dp, bottom = 4.dp, end = 2.dp)
        ) {
            AsyncImageWithFallback(
                admin.avatar,
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
            )

            Gap(4)

            Text(
                text = "@${admin.name}",
                color = colorScheme.onTertiaryContainer,
                fontSize = 14.sp
            )

            Gap(4)

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(
                        colorScheme.onTertiaryContainer.copy(alpha = 0.12f)
                    )
                    .click { onDelete() }
            ) {
                Text(
                    text = "×",
                    color = colorScheme.onTertiaryContainer,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun AddCircleButton(
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(30.dp)
            .clip(CircleShape)
            .background(colorScheme.tertiaryContainer)
            .click(block = onClick)
    ) {
        Text(
            "+",
            color = colorScheme.onTertiaryContainer,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun AvailabilityRuleCard(
    rule: AvailabilityRule,
    onDelete: () -> Unit
) {
    EditableCard(onDelete) {
        Text(
            rule.daysOfWeek.joinToString(", ") { it.name }
        )

        Gap(4)

        Text(
            "${rule.startTime.formatTime()} - ${rule.endTime.formatTime()}"
        )
    }
}

@Composable
private fun OneTimeRuleCard(
    rule: OneTimeAvailabilityRule,
    onDelete: () -> Unit
) {
    EditableCard(onDelete) {
        Text(
            "с ${rule.dateFrom.formatDate()} по ${rule.dateTo.formatDate()}"
        )

        Gap(4)

        Text(
            "${rule.startTime.formatTime()} - ${rule.endTime.formatTime()}"
        )
    }
}

@Composable
private fun AvailabilityBlockCard(
    block: AvailabilityBlock,
    onDelete: () -> Unit
) {
    EditableCard(onDelete) {
        Text(
            "с ${block.from.formatDateTime()} по ${block.to.formatDateTime()}"
        )
    }
}

@Composable
private fun EditableCard(
    onDelete: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shapes.medium)
            .background(colorScheme.surfaceContainer)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            content()
        }

        Box(
            Modifier
                .align(Alignment.TopEnd)
                .clip(CircleShape)
                .click(block = onDelete)
        ) {
            Text(
                "✕",
                color = colorScheme.error,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

fun LocalTime.formatTime(): String =
    hour.toString().padStart(2, '0') +
            ":" +
            minute.toString().padStart(2, '0')

private fun LocalDate.formatDate(): String =
    formatDate(
        LocalDateTime(
            this,
            LocalTime(0, 0)
        ).toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    )

private fun LocalDateTime.formatDateTime(): String =
    formatDateTime(
        toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    )

fun formatDate(epochMillis: Long): String {
    val instant = Instant.fromEpochMilliseconds(epochMillis)
    val date = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date

    return buildString {
        append(date.day.toString().padStart(2, '0'))
        append(".")
        append(date.month.number.toString().padStart(2, '0'))
        append(".")
        append(date.year)
    }
}

fun formatDateTime(epochMillis: Long): String {
    val dateTime = Instant
        .fromEpochMilliseconds(epochMillis)
        .toLocalDateTime(TimeZone.currentSystemDefault())

    return buildString {
        append(dateTime.date.day.toString().padStart(2, '0'))
        append(".")
        append(dateTime.date.month.number.toString().padStart(2, '0'))
        append(".")
        append(dateTime.date.year)

        append(" ")

        append(dateTime.time.hour.toString().padStart(2, '0'))
        append(":")
        append(dateTime.time.minute.toString().padStart(2, '0'))
    }
}

@Composable
fun BookingCard(
    booking: BookingDomain,
    users: Set<UserDomain>,
    onClick: (BookingDomain) -> Unit = {},
    onCancel: (BookingDomain) -> Unit = {}
) {
    val user = users.firstOrNull { it.id == booking.userId }

    Surface(
        color = colorScheme.surface,
        tonalElevation = 2.dp,
        shadowElevation = 2.dp,
        modifier = Modifier
            .clip(shapes.medium)
            .fillMaxWidth()
            .click { onClick(booking) }
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Column {
                    Text(
                        text = "${booking.startsAt.formatDateTime()} - ${booking.endsAt.formatDateTime()}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colorScheme.onSurface
                    )

                    Text(
                        text = booking.status.name,
                        fontSize = 12.sp,
                        color = when (booking.status) {
                            BookingStatus.ACTIVE -> colorScheme.primary
                            BookingStatus.CANCELLED_BY_USER -> colorScheme.error
                            BookingStatus.CANCELLED_BY_ADMIN -> colorScheme.error
                            BookingStatus.CANCELLED_BY_SYSTEM -> colorScheme.error
                            BookingStatus.COMPLETED -> colorScheme.secondary
                        }
                    )
                }

                if (booking.status == BookingStatus.ACTIVE) {
                    TextButton(
                        onClick = { onCancel(booking) }
                    ) {
                        Text("Отменить")
                    }
                }
            }

            Gap(8)

            user?.let {
                AdminChip(it) {}
            }

            booking.cancelReason?.let {
                Gap(8)
                Text(
                    text = "Причина отмены: $it",
                    fontSize = 12.sp,
                    color = colorScheme.error
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> NullableDropdownField(
    value: T?,
    items: Collection<T>,
    onValueChange: (T?) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    nullText: String = "Все",
    itemText: (T) -> String = { it.toString() },
    leading: @Composable ((T) -> Unit)? = null
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = value?.let(itemText) ?: nullText,
            onValueChange = {},
            readOnly = true,
            singleLine = true,
            label = label?.let { { Text(it) } },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            DropdownMenuItem(
                text = {
                    Text(nullText)
                },
                onClick = {
                    onValueChange(null)
                    expanded = false
                }
            )

            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(itemText(item)) },
                    leadingIcon = if (leading == null) null else {
                        { leading(item) }
                    },
                    onClick = {
                        onValueChange(item)
                        expanded = false
                    }
                )
            }
        }
    }
}