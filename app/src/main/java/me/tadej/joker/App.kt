package me.tadej.joker

import android.app.Application
import me.tadej.joker.di.AppComponent
import me.tadej.joker.di.AppModule
import me.tadej.joker.di.DaggerAppComponent
import me.tadej.joker.di.NetworkModule

class App : Application() {
    private lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.builder()
            .networkModule(NetworkModule())
            .appModule(AppModule(this))
            .build()
    }

    fun component(): AppComponent = component
}
