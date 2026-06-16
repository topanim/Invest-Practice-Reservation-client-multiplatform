package app.what.reservation.features.main.domain

import app.what.reservation.core.foundation.core.UIController
import app.what.reservation.data.local.AppValues
import app.what.reservation.features.main.domain.models.MainAction
import app.what.reservation.features.main.domain.models.MainEvent
import app.what.reservation.features.main.domain.models.MainState

class MainController(
    values: AppValues
) : UIController<MainState, MainAction, MainEvent>(
    MainState()
) {
    override fun obtainEvent(viewEvent: MainEvent) = when (viewEvent) {
        else -> {}
    }
}