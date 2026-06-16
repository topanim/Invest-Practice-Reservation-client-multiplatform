package app.what.reservation.core.foundation.ui.forms

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.what.reservation.core.foundation.core.UIComponent
import app.what.reservation.core.foundation.ui.Gap
import app.what.reservation.core.foundation.ui.SegmentTab
import app.what.reservation.core.foundation.ui.capplyIf
import app.what.reservation.core.foundation.ui.click
import app.what.reservation.ui.components.StyledTextField
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus


class Form(
    private vararg val fields: FormField<*>
) {

    val isValid: Boolean
        get() = fields.all { it.isValid }

    fun validate(): Boolean {
        fields.forEach { it.validate() }
        return isValid
    }
}

interface FormField<T> : UIComponent {
    val data: T

    val isValid: Boolean
    val error: String?

    fun validate()
}

abstract class BaseFormField<T>(
    default: T,
    private val validator: (T) -> String? = { null }
) : FormField<T> {

    protected val _data = mutableStateOf(default)
    protected val _error = mutableStateOf<String?>(null)

    override val data: T
        get() = _data.value

    override val error: String?
        get() = _error.value

    override val isValid: Boolean
        get() = error == null

    open fun state(): State<T> = _data

    open fun errorState(): State<String?> = _error

    override fun validate() {
        _error.value = validator(data)
    }

    protected fun update(value: T) {
        _data.value = value
        validate()
    }

    @Composable
    protected fun Title(text: String) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

abstract class BaseListFormField<T>(
    default: List<T>,
    private val validator: (List<T>) -> String? = { null }
) : FormField<List<T>> {

    protected val _data = mutableStateListOf<T>().apply {
        addAll(default)
    }

    protected val _error = mutableStateOf<String?>(null)

    override val data: List<T>
        get() = _data

    override val error: String?
        get() = _error.value

    override val isValid: Boolean
        get() = error == null

    fun state(): SnapshotStateList<T> = _data

    fun errorState(): State<String?> = _error

    override fun validate() {
        _error.value = validator(data)
    }

    protected fun notifyChanged() {
        validate()
    }

    @Composable
    protected fun Title(text: String) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        )
    }
}


class StringFormField(
    private val label: String,
    default: String = "",
    validator: (String) -> String? = { null }
) : BaseFormField<String>(default, validator) {

    @Composable
    override fun content(modifier: Modifier) {
        Column(modifier) {
            Title(label)

            Gap(8)

            StyledTextField(
                value = data,
                onValueChange = { update(it) },
                shape = shapes.medium,
                modifier = Modifier.fillMaxWidth()
            )

            error?.let {
                Gap(4)
                Text(
                    text = it,
                    color = colorScheme.error,
                    fontSize = 12.sp
                )
            }
        }
    }
}

class IntFormField(
    private val label: String,
    default: Int? = null,
    validator: (Int?) -> String? = { null }
) : BaseFormField<Int?>(default, validator) {

    private var text by mutableStateOf(default?.toString().orEmpty())

    @Composable
    override fun content(modifier: Modifier) {
        Column(modifier) {
            Title(label)

            Gap(8)

            StyledTextField(
                value = text,
                onValueChange = {
                    text = it
                    update(it.toIntOrNull())
                },
                filter = {
                    it.isEmpty() || (it.all(Char::isDigit) && !(it.length > 1 && it.startsWith("0")))
                },
                shape = shapes.medium,
                modifier = Modifier.fillMaxWidth()
            )

            error?.let {
                Gap(4)
                Text(
                    it,
                    color = colorScheme.error,
                    fontSize = 12.sp
                )
            }
        }
    }
}

class IntCounterFormField(
    private val name: String,
    default: Int = 0,
    private val valueRange: IntRange,
    private val interval: Int = 1,
    validator: (Int) -> String? = { null }
) : BaseFormField<Int>(default, validator) {

    @Composable
    override fun content(modifier: Modifier) {
        Column(modifier) {
            Title(name)

            Gap(8)

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    enabled = data - interval >= valueRange.first,
                    onClick = {
                        update(data - interval)
                    }
                ) {
                    Text("-", fontSize = 18.sp)
                }

                Gap(8)

                Text(
                    text = data.toString(),
                    fontSize = 16.sp
                )

                Gap(8)

                TextButton(
                    enabled = data + interval <= valueRange.last,
                    onClick = {
                        update(data + interval)
                    }
                ) {
                    Text("+", fontSize = 18.sp)
                }
            }

            error?.let {
                Gap(4)

                Text(
                    text = it,
                    color = colorScheme.error,
                    fontSize = 12.sp
                )
            }
        }
    }
}

class DaysOfWeekFormField(
    private val name: String,
    default: List<DayOfWeek> = emptyList(),
    disabled: List<DayOfWeek> = emptyList(),
    validator: (List<DayOfWeek>) -> String? = { null }
) : BaseListFormField<DayOfWeek>(default, validator) {

    private val disabled = disabled.toSet()

    @Composable
    override fun content(modifier: Modifier) {
        Column(modifier) {
            Title(name)

            Gap(8)

            val days = DayOfWeek.entries

            FlowRow(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                days.forEach { day ->
                    WeekItem(
                        day = day,
                        enabled = day !in disabled,
                        selected = day in data
                    )
                }
            }

            error?.let {
                Gap(4)

                Text(
                    text = it,
                    color = colorScheme.error,
                    fontSize = 12.sp
                )
            }
        }
    }

    @Composable
    private fun WeekItem(
        day: DayOfWeek,
        enabled: Boolean,
        selected: Boolean
    ) {
        Box(
            Modifier
                .clip(shapes.medium)
                .capplyIf(!selected) {
                    border(1.dp, colorScheme.surfaceDim, shapes.medium)
                }
                .background(
                    when {
                        !enabled -> colorScheme.surfaceDim
                        selected -> colorScheme.primaryContainer
                        else -> Color.Transparent
                    }
                )
                .click {
                    if (!enabled) return@click

                    if (selected) {
                        _data.remove(day)
                    } else {
                        _data.add(day)
                    }

                    notifyChanged()
                }
        ) {
            Text(
                day.shortName(),
                modifier = Modifier.padding(16.dp, 12.dp),
                color = when {
                    !enabled -> colorScheme.outline
                    selected -> colorScheme.onPrimaryContainer
                    else -> colorScheme.onBackground
                },
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
            )
        }
    }

    private fun DayOfWeek.shortName(): String =
        when (this) {
            DayOfWeek.MONDAY -> "Пн"
            DayOfWeek.TUESDAY -> "Вт"
            DayOfWeek.WEDNESDAY -> "Ср"
            DayOfWeek.THURSDAY -> "Чт"
            DayOfWeek.FRIDAY -> "Пт"
            DayOfWeek.SATURDAY -> "Сб"
            DayOfWeek.SUNDAY -> "Вс"
        }
}

class SingleChoiceFormField<T>(
    private val name: String,
    values: List<T>,
    default: T? = null,
    private val label: (T) -> String = { it.toString() },
    validator: (T?) -> String? = { null }
) : BaseFormField<T?>(default, validator) {

    private val values = values.toList()

    @Composable
    override fun content(modifier: Modifier) {
        Column(modifier) {
            Title(name)

            Gap(8)

            SingleChoiceSegmentedButtonRow(
                Modifier.fillMaxWidth()
            ) {
                values.forEachIndexed { index, value ->
                    SegmentTab(
                        index, values.size,
                        data == value,
                        null,
                        label(value)
                    ) { update(value) }

                    Gap(8)
                }
            }

            error?.let {
                Gap(4)

                Text(
                    text = it,
                    color = colorScheme.error,
                    fontSize = 12.sp
                )
            }
        }
    }
}

inline fun <reified T : Enum<T>> EnumChoiceFormField(
    name: String,
    default: T? = null,
    noinline label: (T) -> String = { it.name },
    noinline validator: (T?) -> String? = { null }
): SingleChoiceFormField<T> {
    return SingleChoiceFormField(
        name = name,
        values = enumValues<T>().toList(),
        default = default,
        label = label,
        validator = validator
    )
}

class LocalTimeFormField(
    private val name: String,
    private val minuteStep: Int = 15,
    default: LocalTime = LocalTime(9, 0),
    validator: (LocalTime) -> String? = { null }
) : BaseFormField<LocalTime>(default, validator) {

    @Composable
    override fun content(modifier: Modifier) {
        Column(modifier) {
            Title(name)

            Gap(8)

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TimePart(
                    value = data.hour,
                    min = 0,
                    max = 23
                ) { hour ->
                    update(LocalTime(hour, data.minute))
                }

                Gap(8)

                Text(":", fontSize = 20.sp)

                Gap(8)

                TimePart(
                    value = data.minute,
                    min = 0,
                    max = 59,
                    step = minuteStep
                ) { minute ->
                    update(LocalTime(data.hour, minute))
                }
            }

            error?.let {
                Gap(4)

                Text(
                    text = it,
                    color = colorScheme.error,
                    fontSize = 12.sp
                )
            }
        }
    }

    @Composable
    private fun TimePart(
        value: Int,
        min: Int,
        max: Int,
        step: Int = 1,
        onChange: (Int) -> Unit
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                enabled = value - step >= min,
                onClick = {
                    onChange(value - step)
                }
            ) {
                Text("-")
            }

            Text(
                value.toString().padStart(2, '0'),
                fontSize = 18.sp
            )

            TextButton(
                enabled = value + step <= max,
                onClick = {
                    onChange(value + step)
                }
            ) {
                Text("+")
            }
        }
    }
}

class DateRangeFormField(
    fromDefault: LocalDate,
    toDefault: LocalDate
) : FormField<ClosedRange<LocalDate>>, UIComponent {

    val from = DateFormField(
        name = "С",
        default = fromDefault
    )

    val to = DateFormField(
        name = "По",
        default = toDefault
    )

    override val data: ClosedRange<LocalDate>
        get() = from.data..to.data

    override val error: String?
        get() {
            if (from.error != null) return from.error
            if (to.error != null) return to.error

            return if (from.data > to.data)
                "Дата окончания должна быть позже даты начала"
            else
                null
        }

    override val isValid: Boolean
        get() = error == null

    override fun validate() {
        from.validate()
        to.validate()
    }

    @Composable
    override fun content(modifier: Modifier) {
        Column(modifier) {

            from.content(Modifier)

            Gap(16)

            to.content(Modifier)

            error?.let {
                Gap(4)

                Text(
                    text = it,
                    color = colorScheme.error,
                    fontSize = 12.sp
                )
            }
        }
    }
}

class DateFormField(
    private val name: String,
    default: LocalDate,
    validator: (LocalDate) -> String? = { null }
) : BaseFormField<LocalDate>(
    default,
    validator
) {
    private var displayedMonth by mutableStateOf(
        CalendarYearMonth(default.year, default.month)
    )

    @Composable
    override fun content(modifier: Modifier) {
        Column(modifier) {
            Title(name)

            Gap(8)

            Calendar(
                month = displayedMonth,
                selected = data,
                onMonthChange = {
                    displayedMonth = it
                },
                onDateSelected = {
                    update(it)
                }
            )

            error?.let {
                Gap(4)

                Text(
                    text = it,
                    color = colorScheme.error,
                    fontSize = 12.sp
                )
            }
        }
    }
}

data class CalendarYearMonth(
    val year: Int,
    val month: Month
)

fun CalendarYearMonth.plusMonth(): CalendarYearMonth =
    if (month == Month.DECEMBER) {
        CalendarYearMonth(year + 1, Month.JANUARY)
    } else {
        CalendarYearMonth(year, Month.entries[month.ordinal + 1])
    }

fun CalendarYearMonth.minusMonth(): CalendarYearMonth =
    if (month == Month.JANUARY) {
        CalendarYearMonth(year - 1, Month.DECEMBER)
    } else {
        CalendarYearMonth(year, Month.entries[month.ordinal - 1])
    }

data class CalendarCell(
    val date: LocalDate,
    val isCurrentMonth: Boolean
)

private val weekDays = listOf(
    "Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс"
)

private val monthNames = mapOf(
    Month.JANUARY to "Январь",
    Month.FEBRUARY to "Февраль",
    Month.MARCH to "Март",
    Month.APRIL to "Апрель",
    Month.MAY to "Май",
    Month.JUNE to "Июнь",
    Month.JULY to "Июль",
    Month.AUGUST to "Август",
    Month.SEPTEMBER to "Сентябрь",
    Month.OCTOBER to "Октябрь",
    Month.NOVEMBER to "Ноябрь",
    Month.DECEMBER to "Декабрь"
)

fun buildCalendar(month: CalendarYearMonth): List<CalendarCell> {
    val firstDay = LocalDate(month.year, month.month, 1)

    val offset = firstDay.dayOfWeek.isoDayNumber - 1

    val daysInMonth = firstDay
        .plus(1, DateTimeUnit.MONTH)
        .minus(1, DateTimeUnit.DAY)
        .day

    val totalCells =
        if (offset + daysInMonth <= 35) 35
        else 42

    val start = firstDay.minus(offset, DateTimeUnit.DAY)

    return List(totalCells) { index ->
        val date = start.plus(index, DateTimeUnit.DAY)

        CalendarCell(
            date = date,
            isCurrentMonth = date.month == month.month
        )
    }
}

@Composable
fun Calendar(
    month: CalendarYearMonth,
    selected: LocalDate?,
    onMonthChange: (CalendarYearMonth) -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    enabled: (LocalDate) -> Boolean = { true }
) {
    val cells = remember(month) { buildCalendar(month) }

    Column(modifier) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = { onMonthChange(month.minusMonth()) }
            ) {
                Text("←")
            }

            Text(
                text = "${monthNames[month.month]} ${month.year}",
                style = typography.titleMedium
            )

            TextButton(
                onClick = { onMonthChange(month.plusMonth()) }
            ) {
                Text("→")
            }
        }

        Gap(8)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            weekDays.forEach { day ->
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = day,
                        style = typography.labelMedium
                    )
                }
            }
        }

        Gap(8)

        cells.chunked(7).forEach { week ->

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                week.forEach { cell ->

                    val isSelected = selected == cell.date
                    val isEnabled = enabled(cell.date)

                    Surface(
                        onClick = {
                            if (isEnabled) {
                                onDateSelected(cell.date)
                            }
                        },
                        enabled = isEnabled,
                        color = if (isSelected) colorScheme.primaryContainer
                        else Color.Transparent,
                        shape = shapes.small,
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(max = 56.dp)
                            .aspectRatio(1f, true)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = cell.date.day.toString(),
                                color = when {
                                    !cell.isCurrentMonth ->
                                        LocalContentColor.current.copy(alpha = 0.4f)

                                    !isEnabled ->
                                        LocalContentColor.current.copy(alpha = 0.3f)

                                    else ->
                                        LocalContentColor.current
                                }
                            )
                        }
                    }
                }
            }

            Gap(4)
        }
    }
}

class DateTimeRangeFormField(
    fromDate: LocalDate,
    fromTime: LocalTime,
    toDate: LocalDate,
    toTime: LocalTime
) : FormField<ClosedRange<LocalDateTime>> {

    val fromDateField = DateFormField(
        "Дата начала",
        fromDate
    )

    val fromTimeField = LocalTimeFormField(
        "Время начала",
        default = fromTime
    )

    val toDateField = DateFormField(
        "Дата окончания",
        toDate
    )

    val toTimeField = LocalTimeFormField(
        "Время окончания",
        default = toTime
    )

    private val fromDateTime: LocalDateTime
        get() = LocalDateTime(
            fromDateField.data,
            fromTimeField.data
        )

    private val toDateTime: LocalDateTime
        get() = LocalDateTime(
            toDateField.data,
            toTimeField.data
        )

    override val data: ClosedRange<LocalDateTime>
        get() = fromDateTime..toDateTime

    override val error: String?
        get() {
            listOf(
                fromDateField.error,
                fromTimeField.error,
                toDateField.error,
                toTimeField.error
            ).firstOrNull()?.let { return it }

            return if (fromDateTime > toDateTime) {
                "Дата окончания должна быть позже даты начала"
            } else {
                null
            }
        }

    override val isValid: Boolean
        get() = error == null

    override fun validate() {
        fromDateField.validate()
        fromTimeField.validate()
        toDateField.validate()
        toTimeField.validate()
    }

    @Composable
    override fun content(modifier: Modifier) {
        Column(modifier) {
            fromDateField.content(Modifier)

            Gap(8)

            fromTimeField.content(Modifier)

            Gap(8)

            toDateField.content(Modifier)

            Gap(8)

            toTimeField.content(Modifier)


            error?.let {
                Gap(4)

                Text(
                    text = it,
                    color = colorScheme.error,
                    style = typography.bodySmall
                )
            }
        }
    }
}