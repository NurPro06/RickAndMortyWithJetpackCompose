package kg.geeks.rickandmortywithjetpackcompose

import android.app.Application
import kg.geeks.rickandmortywithjetpackcompose.data.modules.dataModule
import kg.geeks.rickandmortywithjetpackcompose.ui.module.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, uiModule)
        }
    }
}