package app.what.reservation.features.serviceDetail.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import app.what.reservation.core.foundation.core.Listener
import app.what.reservation.core.foundation.ui.Gap
import app.what.reservation.core.foundation.ui.Show
import app.what.reservation.core.foundation.ui.click
import app.what.reservation.core.foundation.ui.controllers.rememberDialogController
import app.what.reservation.core.foundation.ui.controllers.rememberSheetController
import app.what.reservation.core.foundation.utils.freeze
import app.what.reservation.features.serviceDetail.domain.models.ServiceDetailEvent
import app.what.reservation.features.serviceDetail.domain.models.ServiceDetailState
import app.what.reservation.features.serviceDetail.presentation.components.BookingCard
import app.what.reservation.features.serviceDetail.presentation.components.BookingsSection
import app.what.reservation.features.serviceDetail.presentation.components.serviceBookingForm
import app.what.reservation.features.serviceDetail.presentation.components.serviceEditingPane
import app.what.reservation.ui.theme.icons.WHATIcons
import app.what.reservation.ui.theme.icons.filled.Run

@Composable
fun ServiceDetailView(
    state: State<ServiceDetailState>,
    listener: Listener<ServiceDetailEvent>
) = Box {
    val sheet = rememberSheetController()
    val dialog = rememberDialogController()

    val bookingForm = serviceBookingForm(state, listener).freeze()
    val serviceEditingPane = serviceEditingPane(state, listener).freeze()

    if (state.value.isAdmin) FloatingActionButton(
        containerColor = colorScheme.tertiaryContainer,
        modifier = Modifier.zIndex(2f).align(Alignment.BottomEnd)
            .padding(bottom = 60.dp, end = 12.dp),
        onClick = {
            sheet.open(true, content = serviceEditingPane)
        }
    ) {
        WHATIcons.Run.Show(colorScheme.onTertiaryContainer)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(max = 800.dp)
                .padding(12.dp, 40.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                state.value.service?.title ?: "Loading...",
                color = colorScheme.primary,
                fontSize = 36.sp,
                lineHeight = 36.sp,
                fontWeight = FontWeight.Bold
            )

            Gap(8)

            Text(
                "${state.value.service?.durationMinutes} мин | ${state.value.service?.price} ₽",
                color = colorScheme.secondary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )

            Gap(12)

            Text(
                state.value.service?.description ?: "Loading...",
                color = colorScheme.onSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )

            Gap(12)

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .clip(shapes.medium)
                    .background(colorScheme.primaryContainer)
                    .click { dialog.open(true, content = bookingForm) },
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(16.dp, 8.dp)
                ) {
                    Text(
                        "Забронировать",
                        color = colorScheme.onPrimaryContainer,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }

            Gap(12)

            BookingsSection("Мои брони") {
                state.value.myReservations.forEach { booking ->
                    BookingCard(
                        booking = booking,
                        users = state.value.userCacheInfo,
                        onClick = {

                        },
                        onCancel = {
                            listener(ServiceDetailEvent.OnCancelBookingAsUser(it.id))
                        }
                    )

                    Gap(8)
                }
            }
        }
    }
}