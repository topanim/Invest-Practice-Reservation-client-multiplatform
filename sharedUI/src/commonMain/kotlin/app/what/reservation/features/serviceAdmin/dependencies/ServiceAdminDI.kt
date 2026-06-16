package app.what.reservation.features.serviceAdmin.dependencies

import app.what.reservation.features.serviceAdmin.domain.ServiceAdminController
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf

val serviceAdminModule: Module.() -> Unit = {
    singleOf(::ServiceAdminController)
}