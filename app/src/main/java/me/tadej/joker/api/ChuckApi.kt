package me.tadej.joker.api

import me.tadej.joker.parser.Parser
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject

class ChuckApi @Inject constructor(
    private val client: OkHttpClient,
    private val parser: Parser<String, String>
) : FunnyApi {
    override suspend fun tellJoke(): String {
        val request = Request.Builder()
            .url(URL)
            .build()
        val response = client.newCall(request).execute()
        return if (response.isSuccessful) {
            val body = response.body() ?: return DEFAULT_RESPONSE
            parser.parse(body.string())
        } else {
            DEFAULT_RESPONSE
        }
    }

    companion object {
        private const val URL = "https://api.chucknorris.io/jokes/random"
        private const val DEFAULT_RESPONSE = ""
    }
}
