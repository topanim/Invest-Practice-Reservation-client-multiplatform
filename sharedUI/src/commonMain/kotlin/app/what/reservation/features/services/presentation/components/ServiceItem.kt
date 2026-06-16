package app.what.reservation.features.services.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.what.reservation.core.foundation.ui.Gap
import app.what.reservation.core.foundation.ui.click
import app.what.reservation.domain.models.ServiceDomain

@Composable
fun ServiceItem(
    item: ServiceDomain,
    onClick: () -> Unit
) = Box(
    Modifier
        .clip(shapes.medium)
        .background(colorScheme.surfaceContainer)
        .click(block = onClick)
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth().padding(12.dp)
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                item.title,
                color = colorScheme.onSecondaryContainer,
                fontSize = 16.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Gap(8)
            Text(
                item.description ?: "No description",
                color = colorScheme.onSecondaryContainer,
                fontSize = 14.sp,
                lineHeight = 14.sp,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
        }

        Column {
            Text(
                "${item.price} ₽",
                color = colorScheme.onSecondaryContainer,
                fontSize = 16.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Gap(8)
            Text(
                "${item.durationMinutes} мин",
                color = colorScheme.onSecondaryContainer,
                fontSize = 16.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Gap(8)
            Text(
                "${item.bufferMinutes} мин",
                color = colorScheme.tertiary,
                fontSize = 16.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}