package app.what.reservation.androidApp

import android.app.Application
import android.content.Context
import app.what.reservation.di.initKoin
import org.koin.dsl.module

class App: Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin(
            module = module {
                single<Context> { this@App }
            }
        )
    }
}