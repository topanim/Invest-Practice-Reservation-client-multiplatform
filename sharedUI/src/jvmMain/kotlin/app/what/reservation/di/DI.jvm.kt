package app.what.reservation.di

import app.what.reservation.core.foundation.data.settings.Preference
import app.what.reservation.data.JvmPreference
import app.what.reservation.infrastructure.auth.BrowserLauncher
import app.what.reservation.infrastructure.auth.JvmBrowserLauncher
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<Preference> { JvmPreference() }
    single<HttpClientEngine> { OkHttp.create() }
    singleOf(::JvmBrowserLauncher).bind(BrowserLauncher::class)
}