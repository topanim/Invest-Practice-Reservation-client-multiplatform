package app.what.reservation.infrastructure.auth

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

class AndroidBrowserLauncher(
    private val context: Context
) : BrowserLauncher {
    override fun open(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri()).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        context.startActivity(intent)
    }
}