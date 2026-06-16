package app.what.reservation.infrastructure.auth

import kotlinx.browser.window

class WebBrowserLauncher : BrowserLauncher {
    override fun open(url: String) {
        window.open(url, "_self")
    }
}