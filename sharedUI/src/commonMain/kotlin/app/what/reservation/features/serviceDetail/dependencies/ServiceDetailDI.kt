package app.what.reservation.features.serviceDetail.dependencies

import app.what.reservation.features.serviceDetail.domain.ServiceDetailController
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf

val serviceDetailModule: Module.() -> Unit = {
    factoryOf(::ServiceDetailController)
}