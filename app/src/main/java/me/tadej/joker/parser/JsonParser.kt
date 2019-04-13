package me.tadej.joker.parser

import org.json.JSONObject
import javax.inject.Inject

class JsonParser @Inject constructor() : Parser<String, String> {
    override fun parse(data: String): String {
        val obj = JSONObject(data)
        return obj.getString("value")
    }
}
