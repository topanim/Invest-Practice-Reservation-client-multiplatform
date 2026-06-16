package app.what.reservation.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

val LocalNavController = compositionLocalOf<Navigator?> { null }

data class Navigator(
    val parent: Navigator?,
    val c: NavHostController
)

@Composable
fun rememberHostNavigator(
    parent: Navigator? = LocalNavController.current
): Navigator {
    val controller = rememberNavController()
    return remember { Navigator(parent, controller) }
}

@Composable
fun rememberNavigator(): Navigator =
    LocalNavController.current ?: error("no navigator")

@Composable
fun rememberNavigator(level: Int): Navigator {
    val current = LocalNavController.current
    var parentNavigator = current

    repeat(level) {
        parentNavigator ?: return@repeat
        parentNavigator = parentNavigator.parent
    }

    return parentNavigator ?: error("no navigator")
}
