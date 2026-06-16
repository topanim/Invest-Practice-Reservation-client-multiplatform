package app.what.reservation.infrastructure.auth

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

class NativeBrowserLauncher : BrowserLauncher {
    override fun open(url: String) {
        NSURL.URLWithString(url)?.let {
            UIApplication.sharedApplication.openURL(it)
        }
    }
}