package com.example.project1_438

import java.util.LinkedHashMap
import java.util.Locale

internal object DictionaryRepository {
    private const val MAX_CACHE_SIZE = 2

    private val cache = object : LinkedHashMap<String, DictionaryEntry>(
        MAX_CACHE_SIZE,
        0.75f,
        true,
    ) {
        override fun removeEldestEntry(
            eldest: MutableMap.MutableEntry<String, DictionaryEntry>?,
        ): Boolean = size > MAX_CACHE_SIZE
    }

    @Synchronized
    fun lookUp(word: String): DictionaryEntry {
        val cacheKey = word.trim().lowercase(Locale.ROOT)

        cache[cacheKey]?.let { return it }

        return DictionaryApi.lookUp(word).also { entry ->
            cache[cacheKey] = entry
        }
    }
}
