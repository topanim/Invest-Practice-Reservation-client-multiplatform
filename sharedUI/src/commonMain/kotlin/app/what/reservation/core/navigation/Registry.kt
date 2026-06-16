package app.what.reservation.core.navigation

import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import app.what.reservation.core.foundation.core.Feature

typealias Registry = NavGraphBuilder.() -> Unit

inline fun <reified P : NavProvider, S : NavComponent<P>> NavGraphBuilder.register(crossinline screen: (P) -> Feature<*, *>) {
    composable<P> {
        val s = remember { screen(it.toRoute<P>()) }
        s.content(Modifier)
    }
}