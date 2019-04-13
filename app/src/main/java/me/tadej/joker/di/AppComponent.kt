package me.tadej.joker.di

import dagger.Component
import me.tadej.joker.MainActivity
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        NetworkModule::class,
        ParserModule::class,
        ApiModule::class
    ]
)
interface AppComponent {
    fun inject(activity: MainActivity)
}
