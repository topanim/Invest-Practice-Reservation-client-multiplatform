package app.what.reservation.features.services.dependencies

import app.what.reservation.features.services.domain.ServicesController
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf

val servicesModule: Module.() -> Unit = {
    singleOf(::ServicesController)
}