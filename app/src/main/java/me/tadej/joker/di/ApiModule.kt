package me.tadej.joker.di

import dagger.Binds
import dagger.Module
import me.tadej.joker.api.ChuckApi
import me.tadej.joker.api.FunnyApi

@Module
interface ApiModule {
    @Binds fun providesFunnyApi(api: ChuckApi): FunnyApi
}
