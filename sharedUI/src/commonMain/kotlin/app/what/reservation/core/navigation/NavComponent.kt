package app.what.reservation.core.navigation

import app.what.reservation.core.foundation.core.UIComponent

interface NavComponent<P : NavProvider> : UIComponent {
    val data: P
}