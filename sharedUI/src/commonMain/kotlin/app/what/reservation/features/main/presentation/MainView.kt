package app.what.reservation.features.main.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.what.reservation.core.foundation.core.Listener
import app.what.reservation.core.foundation.ui.animations.AnimatedEnter
import app.what.reservation.core.navigation.NavigationHost
import app.what.reservation.core.navigation.bottom_navigation.BottomNavBar
import app.what.reservation.core.navigation.bottom_navigation.NavAction
import app.what.reservation.core.navigation.rememberHostNavigator
import app.what.reservation.features.main.MainFeature
import app.what.reservation.features.main.domain.models.MainEvent
import app.what.reservation.features.main.domain.models.MainState
import app.what.reservation.ui.theme.icons.WHATIcons
import app.what.reservation.ui.theme.icons.filled.FrameBug
import app.what.reservation.utils.Screens
import app.what.reservation.utils.rememberAppValues

@Composable
fun MainView(
    state: State<MainState>,
    listener: Listener<MainEvent>
) = Column(
    modifier = Modifier.fillMaxSize()
) {
    val navigator = rememberHostNavigator()
    val appValues = rememberAppValues()
    val devFeaturesEnabled by appValues.devPanelEnabled.collect()
//        var showBottomNavBar by useState(true)

    Box(
        Modifier
            .fillMaxSize()
            .background(colorScheme.background)
    ) {
        NavigationHost(
            navigator = navigator,
            start = Screens.Profile,
            registry = MainFeature.childrenRegistry
        )

        AnimatedEnter(
//                showBottomNavBar,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            BottomNavBar(
                navigator = navigator,
                screens = MainFeature.children,
            ) {
//                if (!devFeaturesEnabled!!) null
//                else
                NavAction("Для разработчиков", WHATIcons.FrameBug) {
                    navigator.c.navigate(Screens.Dev)
                }
            }
        }
    }
}