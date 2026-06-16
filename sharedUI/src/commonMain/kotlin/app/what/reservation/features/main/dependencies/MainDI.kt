package app.what.reservation.features.main.dependencies

import app.what.reservation.features.main.domain.MainController
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf

val mainModule: Module.() -> Unit = {
    singleOf(::MainController)
}