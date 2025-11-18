package org.example.cache

object RedisKeyProvider {
    private const val BANK_MUTEX_KEY = "bankMutex"
    private const val HISTORY_CACHE_KEY = "history"

    fun bankMutexKey(ulid: String, accountUlid: String) : String {
        return "$BANK_MUTEX_KEY:$ulid:$accountUlid"
    }

    fun historyCacheKey(ulid: String, accountUlid: String) : String {
        return "$HISTORY_CACHE_KEY:$ulid:$accountUlid"
    }
}