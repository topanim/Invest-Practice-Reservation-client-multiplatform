package app.what.reservation.features.profile.dependencies

import app.what.reservation.features.profile.domain.ProfileController
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf

val profileModule: Module.() -> Unit = {
    singleOf(::ProfileController)
}