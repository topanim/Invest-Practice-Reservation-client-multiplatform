package app.what.reservation.di

import app.what.reservation.data.local.AppValues
import app.what.reservation.data.local.AuthSessionManager
import app.what.reservation.data.local.AuthStorage
import app.what.reservation.data.remote.AuthService
import app.what.reservation.data.remote.AvailabilityService
import app.what.reservation.data.remote.BookingsService
import app.what.reservation.data.remote.ServicesService
import app.what.reservation.data.remote.UsersService
import app.what.reservation.features.auth.dependencies.authModule
import app.what.reservation.features.dev.presentation.NetworkMonitorPlugin
import app.what.reservation.features.main.dependencies.mainModule
import app.what.reservation.features.profile.dependencies.profileModule
import app.what.reservation.features.serviceAdmin.dependencies.serviceAdminModule
import app.what.reservation.features.serviceDetail.dependencies.serviceDetailModule
import app.what.reservation.features.services.dependencies.servicesModule
import app.what.reservation.infrastructure.auth.OAuthCallbackServer
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.includes
import org.koin.dsl.module

fun initKoin(configuration: KoinAppDeclaration? = null, module: Module = module { }) {
    startKoin {
        configuration?.let { includes(it) }
        modules(platformModule, commonModule, module)
    }
}

expect val platformModule: Module

val coreModule: Module.() -> Unit = {
    single {
        HttpClient(get<HttpClientEngine>()) {
            followRedirects = false

            install(NetworkMonitorPlugin)
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                })
            }
        }
    }
}

val infrastructureModule: Module.() -> Unit = {
    singleOf(::OAuthCallbackServer)
}

val dataModule: Module.() -> Unit = {
    singleOf(::AppValues)
    singleOf(::AuthStorage)
    singleOf(::AuthSessionManager)

    // ApiServices
    singleOf(::AuthService)
    singleOf(::UsersService)
    singleOf(::ServicesService)
    singleOf(::AvailabilityService)
    singleOf(::BookingsService)
}

val domainModule: Module.() -> Unit = {

}

val featuresModule: Module.() -> Unit = {
    authModule()
    mainModule()
    profileModule()
    servicesModule()
    serviceDetailModule()
    serviceAdminModule()
}

val commonModule = module {
    infrastructureModule()
    coreModule()
    dataModule()
    domainModule()
    featuresModule()
}