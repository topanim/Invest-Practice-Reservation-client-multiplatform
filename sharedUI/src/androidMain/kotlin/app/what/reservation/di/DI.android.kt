package app.what.reservation.di

import android.content.Context
import app.what.reservation.core.foundation.data.settings.Preference
import app.what.reservation.data.AndroidPreference
import app.what.reservation.infrastructure.auth.AndroidBrowserLauncher
import app.what.reservation.infrastructure.auth.BrowserLauncher
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import okhttp3.internal.platform.AndroidPlatform
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<Preference> {
        val prefs = get<Context>().getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        AndroidPreference(prefs)
    }
    single<HttpClientEngine> { OkHttp.create() }
    singleOf(::AndroidBrowserLauncher).bind(BrowserLauncher::class)
}