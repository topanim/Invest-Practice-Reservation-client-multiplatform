package app.what.reservation.di

import app.what.reservation.data.WebPreference
import app.what.reservation.infrastructure.auth.BrowserLauncher
import app.what.reservation.infrastructure.auth.WebBrowserLauncher
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule: Module = module {
    singleOf(::WebPreference)
    singleOf(::WebBrowserLauncher).bind(BrowserLauncher::class)
}