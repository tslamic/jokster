package me.tadej.joker.api

interface FunnyApi {
    suspend fun tellJoke(): String
}
