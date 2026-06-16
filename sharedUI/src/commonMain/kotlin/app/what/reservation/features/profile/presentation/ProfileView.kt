package app.what.reservation.features.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import app.what.reservation.core.foundation.core.Listener
import app.what.reservation.core.foundation.ui.Gap
import app.what.reservation.core.foundation.ui.click
import app.what.reservation.features.profile.domain.models.ProfileEvent
import app.what.reservation.features.profile.domain.models.ProfileState
import app.what.reservation.features.serviceDetail.presentation.components.BookingCard
import app.what.reservation.features.serviceDetail.presentation.components.BookingsSection
import app.what.reservation.ui.components.AsyncImageWithFallback
import app.what.reservation.ui.theme.Strings

@Composable
fun ProfileView(
    state: State<ProfileState>,
    listener: Listener<ProfileEvent>
) = Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.fillMaxSize()
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .widthIn(max = 800.dp)
            .padding(12.dp, 40.dp)
            .padding(bottom = 50.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImageWithFallback(
                state.value.info?.picture,
                Modifier.size(100.dp).clip(shapes.medium),
                enableDetailView = true
            )

            Gap(12)

            Column {
                Text(
                    text = state.value.info?.name ?: "Loading...",
                    fontSize = 52.sp,
                    lineHeight = 52.sp,
                    color = colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = state.value.info?.email ?: "Loading...",
                    fontSize = 20.sp,
                    lineHeight = 20.sp,
                    color = colorScheme.secondary,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Gap(12)

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clip(shapes.medium)
                .background(colorScheme.primaryContainer)
                .click { listener(ProfileEvent.OnRefresh) },
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp, 8.dp)
            ) {
                Text(
                    Strings.Default.refresh,
                    color = colorScheme.onPrimaryContainer,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
        }

        Gap(8)

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clip(shapes.medium)
                .background(colorScheme.errorContainer)
                .click { listener(ProfileEvent.OnLogout) },
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp, 8.dp)
            ) {
                Text(
                    Strings.Default.logout,
                    color = colorScheme.onErrorContainer,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
        }

        Gap(8)

        BookingsSection("Мои брони") {
            FlowRow(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                maxItemsInEachRow = 2
            ) {
                state.value.myReservations.forEach { booking ->
                    BookingCard(
                        booking = booking,
                        users = state.value.userCacheInfo,
                        onClick = {

                        },
                        onCancel = {
                            listener(ProfileEvent.OnCancelBookingAsUser(it.id))
                        }
                    )

                    Gap(8)
                }
            }
        }
    }
}