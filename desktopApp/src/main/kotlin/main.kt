import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import app.what.reservation.App
import app.what.reservation.di.initKoin
import app.what.reservation.utils.AppDispatchers
import java.awt.Dimension

fun main() {
    initKoin()
    AppDispatchers.IO
    application {
        Window(
            title = "Reservation",
            state = rememberWindowState(width = 800.dp, height = 600.dp),
            onCloseRequest = ::exitApplication,
        ) {
            window.minimumSize = Dimension(350, 600)
            App()
        }
    }
}
