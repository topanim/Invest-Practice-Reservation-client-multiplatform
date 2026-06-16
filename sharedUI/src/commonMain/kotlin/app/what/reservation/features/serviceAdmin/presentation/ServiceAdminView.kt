package app.what.reservation.features.serviceAdmin.presentation

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import app.what.reservation.core.foundation.core.Listener
import app.what.reservation.core.foundation.ui.Gap
import app.what.reservation.core.foundation.ui.SegmentTab
import app.what.reservation.core.foundation.ui.Show
import app.what.reservation.core.foundation.ui.click
import app.what.reservation.core.foundation.ui.controllers.rememberDialogController
import app.what.reservation.features.serviceAdmin.domain.models.ServiceAdminEvent
import app.what.reservation.features.serviceAdmin.domain.models.ServiceAdminState
import app.what.reservation.features.serviceAdmin.presentation.components.serviceCreateForm
import app.what.reservation.features.services.presentation.components.ServiceItem
import app.what.reservation.ui.components.AsyncImageWithFallback
import app.what.reservation.ui.theme.Strings
import app.what.reservation.ui.theme.icons.WHATIcons
import app.what.reservation.ui.theme.icons.filled.Export
import kotlinx.coroutines.launch

private enum class ServiceAdminPages {
    Services,
    Clients
}

@Composable
fun ServiceAdminView(
    state: State<ServiceAdminState>,
    listener: Listener<ServiceAdminEvent>
) = Box {
    val dialog = rememberDialogController()
    val form = remember { serviceCreateForm(state, listener) }

    FloatingActionButton(
        containerColor = colorScheme.tertiaryContainer,
        modifier = Modifier.zIndex(2f)
            .align(Alignment.BottomEnd)
            .padding(bottom = 60.dp, end = 12.dp)
            .systemBarsPadding(),
        onClick = { dialog.open(true, content = form) }
    ) {
        WHATIcons.Export.Show(colorScheme.onTertiaryContainer)
    }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        val scope = rememberCoroutineScope()
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(max = 800.dp)
                .padding(12.dp, 40.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .clip(shapes.medium)
                    .background(colorScheme.primaryContainer)
                    .click { listener(ServiceAdminEvent.OnServicesListRefresh) },
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

            Gap(8.dp)

            val pagerState =
                rememberPagerState(ServiceAdminPages.Services.ordinal) { ServiceAdminPages.entries.size }

            SingleChoiceSegmentedButtonRow(
                space = (-4).dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                ServiceAdminPages.entries.forEachIndexed { i, it ->
                    SegmentTab(
                        i, ServiceAdminPages.entries.size,
                        selected = pagerState.currentPage == it.ordinal,
                        icon = null,
                        when (it) {
                            ServiceAdminPages.Services -> "Сервисы"
                            ServiceAdminPages.Clients -> "Пользователи"
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
                    ServiceAdminPages.Services.ordinal -> ServicesSection(state, listener)
                    ServiceAdminPages.Clients.ordinal -> UsersSection(state, listener)
                    else -> Unit
                }
            }
        }
    }
}

@Composable
fun ServicesSection(
    state: State<ServiceAdminState>,
    listener: Listener<ServiceAdminEvent>
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(state.value.services, { it.id }) {
            ServiceItem(it) {
                listener(ServiceAdminEvent.OnServiceClicked(it))
            }
        }
    }
}

@Composable
fun UsersSection(
    state: State<ServiceAdminState>,
    listener: Listener<ServiceAdminEvent>
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(state.value.clients, { it.id }) {
            Box(
                Modifier.clip(CircleShape).background(colorScheme.surfaceContainer)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        AsyncImageWithFallback(
                            it.avatar,
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(54.dp)
                        )

                        Gap(8)
                        Column {
                            Text(
                                it.name,
                                color = colorScheme.onSurface,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )

                            Text(
                                it.email,
                                color = colorScheme.secondary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }

                    TextButton(
                        onClick = {

                        }
                    ) {
                        Text("Бан")
                    }
                }
            }
        }
    }
}