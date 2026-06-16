package app.what.reservation.infrastructure.auth

import java.awt.Desktop
import java.net.URI

class JvmBrowserLauncher : BrowserLauncher{
    override fun open(url: String) {
        Desktop.getDesktop().browse(URI(url))
    }
}