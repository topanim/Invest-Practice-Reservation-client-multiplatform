package app.what.reservation.features.services.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.what.reservation.core.foundation.core.Listener
import app.what.reservation.core.foundation.services.AppLogger.Companion.Auditor
import app.what.reservation.core.foundation.ui.Gap
import app.what.reservation.core.foundation.ui.Show
import app.what.reservation.core.foundation.ui.click
import app.what.reservation.core.foundation.ui.useState
import app.what.reservation.features.services.domain.models.ServicesEvent
import app.what.reservation.features.services.domain.models.ServicesState
import app.what.reservation.features.services.presentation.components.ServiceItem
import app.what.reservation.ui.components.StyledTextField
import app.what.reservation.ui.theme.icons.WHATIcons
import app.what.reservation.ui.theme.icons.filled.FrameBug

@Composable
fun ServicesView(
    state: State<ServicesState>,
    listener: Listener<ServicesEvent>
) = Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.fillMaxSize()
) {
    val (search, setSearch) = useState("")

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .widthIn(max = 800.dp)
            .padding(12.dp, 40.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            StyledTextField(
                search, setSearch,
                debounce = 500,
                modifier = Modifier.fillMaxWidth().weight(1f),
                placeholder = "Поиск"
            )

            WHATIcons.FrameBug.Show(
                colorScheme.primary, 28,
                modifier = Modifier.padding(horizontal = 12.dp).click {
                    Auditor.debug("d", "services refresh clicked")
                    listener(ServicesEvent.OnServicesListRefresh)
                }
            )
        }

        Gap(12)

        state.value.services.filter {
            search in it.title || search in it.description
        }.forEach {
            ServiceItem(it) { listener(ServicesEvent.OnServiceClicked(it)) }
            Gap(12)
        }
    }
}
