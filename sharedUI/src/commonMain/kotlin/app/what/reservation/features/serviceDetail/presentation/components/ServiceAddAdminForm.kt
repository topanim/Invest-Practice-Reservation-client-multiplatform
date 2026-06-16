package app.what.reservation.features.serviceDetail.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.what.reservation.core.foundation.core.Listener
import app.what.reservation.core.foundation.ui.Gap
import app.what.reservation.core.foundation.ui.Show
import app.what.reservation.core.foundation.ui.click
import app.what.reservation.core.foundation.ui.forms.StringFormField
import app.what.reservation.core.foundation.ui.toggle
import app.what.reservation.core.foundation.ui.useStateList
import app.what.reservation.core.foundation.utils.freeze
import app.what.reservation.domain.models.UserDomain
import app.what.reservation.features.serviceDetail.domain.models.ServiceDetailEvent
import app.what.reservation.features.serviceDetail.domain.models.ServiceDetailState
import app.what.reservation.ui.components.AsyncImageWithFallback
import app.what.reservation.ui.theme.Strings
import app.what.reservation.ui.theme.icons.WHATIcons
import app.what.reservation.ui.theme.icons.filled.Support

fun serviceAddAdminForm(
    state: State<ServiceDetailState>,
    listener: Listener<ServiceDetailEvent>
) = @Composable {
    val selectedUsers = useStateList<UserDomain>()

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
                .widthIn(max = 800.dp)
                .padding(12.dp, 8.dp)
                .padding(top = 12.dp)
        ) {
            val queryInput = StringFormField("Поиск...").freeze()
            val query = queryInput.state()
            LaunchedEffect(query.value) {
                listener(ServiceDetailEvent.OnUserSearchConfirm(query.value))
            }

            queryInput.content(Modifier)

            state.value.userSearchResult.forEach {
                UserSearchItem(it, it in selectedUsers, it in state.value.admins) {
                    selectedUsers.toggle(it)
                }
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = shapes.medium,
                onClick = {
                    listener(ServiceDetailEvent.OnAddAdminFormConfirmed(selectedUsers.map(UserDomain::id)))
                }
            ) {
                Text(Strings.Default.add)
            }
        }
    }
}

@Composable
private fun UserSearchItem(
    user: UserDomain,
    selected: Boolean,
    disabled: Boolean,
    onClick: () -> Unit
) {
    Box(
        Modifier.clip(CircleShape)
            .background(colorScheme.primaryContainer)
            .click(enabled = !disabled, block = onClick)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            AnimatedContent(selected || disabled) {
                if (!it) AsyncImageWithFallback(
                    user.avatar,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(54.dp)
                ) else Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(colorScheme.primary)
                ) {
                    WHATIcons.Support.Show(colorScheme.background, 32)
                }
            }

            Gap(8)
            Column {
                Text(
                    user.name,
                    color = colorScheme.onPrimaryContainer,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    user.email,
                    color = colorScheme.secondary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}