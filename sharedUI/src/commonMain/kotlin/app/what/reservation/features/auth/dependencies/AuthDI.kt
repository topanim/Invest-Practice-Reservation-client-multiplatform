package app.what.reservation.features.auth.dependencies

import app.what.reservation.features.auth.domain.AuthController
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf

val authModule: Module.() -> Unit = {
    singleOf(::AuthController)
}