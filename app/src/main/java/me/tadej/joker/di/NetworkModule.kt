package me.tadej.joker.di

import android.content.Context
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
class NetworkModule {
    @Provides @Singleton fun providesOkHttpClient(context: Context): OkHttpClient {
        val cache = Cache(context.cacheDir, CACHE_SIZE)
        return OkHttpClient.Builder()
            .cache(cache)
            .build()
    }

    companion object {
        private const val CACHE_SIZE: Long = 1024 * 1024
    }
}
