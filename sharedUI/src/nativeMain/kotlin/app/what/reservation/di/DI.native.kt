package app.what.reservation.di

import app.what.reservation.core.foundation.data.settings.Preference
import app.what.reservation.data.NativePreference
import app.what.reservation.infrastructure.auth.BrowserLauncher
import app.what.reservation.infrastructure.auth.NativeBrowserLauncher
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule: Module = module {
    singleOf(::NativePreference)
    single<HttpClientEngine> { Darwin.create() }
    singleOf(::NativeBrowserLauncher).bind(BrowserLauncher::class)
}