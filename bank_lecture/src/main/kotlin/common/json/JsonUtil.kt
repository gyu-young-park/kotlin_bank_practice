package org.example.common.json

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

// object로 만들면 싱글톤 구현가능
object JsonUtil {
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    fun <T> encodeToJson(v: T, serializer: KSerializer<T>) : String {
        return json.encodeToString(serializer, v)
    }

    fun <T> decodeFromJson(v: String, serializer: KSerializer<T>) : T {
        return json.decodeFromString(serializer, v)
    }
}