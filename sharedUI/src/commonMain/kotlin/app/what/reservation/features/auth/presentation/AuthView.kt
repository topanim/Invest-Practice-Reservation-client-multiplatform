package app.what.reservation.features.auth.presentation

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
import androidx.compose.foundation.layout.widthIn
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
import app.what.reservation.core.foundation.ui.click
import app.what.reservation.features.auth.domain.models.AuthEvent
import app.what.reservation.features.auth.domain.models.AuthState
import app.what.reservation.ui.theme.Strings

@Composable
fun AuthView(
    state: State<AuthState>,
    listener: Listener<AuthEvent>
) = Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.fillMaxSize()
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxHeight().widthIn(max = 800.dp).padding(12.dp, 40.dp)
    ) {
        Text(
            text = Strings.Default.greetings,
            fontSize = 54.sp,
            lineHeight = 54.sp,
            color = colorScheme.primary,
            fontWeight = FontWeight.Bold
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clip(shapes.medium)
                .background(colorScheme.primary)
                .click {
                    listener(AuthEvent.OnSignWithGoogleClicked)
                },
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp, 8.dp)
            ) {
                Text(
                    Strings.Default.signWithGoogle,
                    color = colorScheme.onPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}