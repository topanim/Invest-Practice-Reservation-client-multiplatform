package app.what.reservation.features.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import app.what.reservation.core.foundation.core.Feature
import app.what.reservation.core.foundation.services.AppLogger.Companion.Auditor
import app.what.reservation.core.navigation.NavComponent
import app.what.reservation.core.navigation.Registry
import app.what.reservation.core.navigation.bottom_navigation.NavItem
import app.what.reservation.core.navigation.bottom_navigation.navItem
import app.what.reservation.features.dev.navigation.devRegistry
import app.what.reservation.features.main.domain.MainController
import app.what.reservation.features.main.domain.models.MainEvent
import app.what.reservation.features.main.presentation.MainView
import app.what.reservation.features.profile.dependencies.profileRegistry
import app.what.reservation.features.serviceAdmin.dependencies.serviceAdminRegistry
import app.what.reservation.features.services.dependencies.servicesRegistry
import app.what.reservation.ui.theme.icons.WHATIcons
import app.what.reservation.ui.theme.icons.filled.Building
import app.what.reservation.ui.theme.icons.filled.Person
import app.what.reservation.ui.theme.icons.filled.Support
import app.what.reservation.utils.Screens
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainFeature(
    override val data: Screens.Main
) : Feature<MainController, MainEvent>(),
    NavComponent<Screens.Main>,
    KoinComponent {

    companion object {
        val children: List<NavItem> = mutableListOf(
            navItem("Услуги", WHATIcons.Building, Screens.Services),
            navItem("Админская", WHATIcons.Support, Screens.ServiceAdmin),
            navItem("Профиль", WHATIcons.Person, Screens.Profile),
        )

        val childrenRegistry: Registry = {
            servicesRegistry()
            serviceAdminRegistry()
            profileRegistry()
            devRegistry()
        }
    }

    override val controller: MainController by inject()

    @Composable
    override fun content(modifier: Modifier) {
        val state = controller.collectStates()
        val action = controller.collectActions()

        LaunchedEffect(Unit) {
            Auditor.debug("main", "MainFeature composed")
        }

        MainView(state, listener)

        LaunchedEffect(action.value) {
            when (action.value) {
                null -> Unit
            }
            controller.clearAction()
        }
    }
}