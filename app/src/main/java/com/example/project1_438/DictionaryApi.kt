package com.example.project1_438

import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URLEncoder
import java.net.URL
import java.nio.charset.StandardCharsets

internal data class DictionaryEntry(
    val word: String,
    val phonetics: List<String>,
    val meanings: List<DictionaryMeaning>,
    val origin: String?,
    val sourceUrls: List<String>,
    val license: DictionaryLicense?,
) {
    fun firstDefinition(): String? =
        meanings.firstNotNullOfOrNull { meaning ->
            meaning.definitions.firstOrNull()?.text
        }
}

internal data class DictionaryMeaning(
    val partOfSpeech: String,
    val definitions: List<DictionaryDefinition>,
    val synonyms: List<String>,
    val antonyms: List<String>,
)

internal data class DictionaryDefinition(
    val text: String,
    val example: String?,
    val synonyms: List<String>,
    val antonyms: List<String>,
)

internal data class DictionaryLicense(
    val name: String,
    val url: String?,
)

internal object DictionaryApi {
    private const val BASE_URL =
        "https://api.dictionaryapi.dev/api/v2/entries/en"

    fun lookUp(word: String): DictionaryEntry {
        val encodedWord = URLEncoder.encode(
            word.trim(),
            StandardCharsets.UTF_8.name(),
        ).replace("+", "%20")

        val connection = URL("$BASE_URL/$encodedWord")
            .openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.setRequestProperty("Accept", "application/json")

        return try {
            if (connection.responseCode !in 200..299) {
                throw IOException("Dictionary request failed with ${connection.responseCode}")
            }

            val response = connection.inputStream.bufferedReader().use { it.readText() }
            parse(response)
        } finally {
            connection.disconnect()
        }
    }

    internal fun parse(response: String): DictionaryEntry {
        val entry = JSONArray(response).getJSONObject(0)
        val meanings = mutableListOf<DictionaryMeaning>()
        val meaningsJson = entry.optJSONArray("meanings") ?: JSONArray()

        for (meaningIndex in 0 until meaningsJson.length()) {
            val meaningJson = meaningsJson.getJSONObject(meaningIndex)
            val definitions = mutableListOf<DictionaryDefinition>()
            val definitionsJson = meaningJson.optJSONArray("definitions") ?: JSONArray()

            for (definitionIndex in 0 until definitionsJson.length()) {
                val definitionJson = definitionsJson.getJSONObject(definitionIndex)
                val definition = definitionJson.optionalString("definition") ?: continue

                definitions += DictionaryDefinition(
                    text = definition,
                    example = definitionJson.optionalString("example"),
                    synonyms = definitionJson.stringList("synonyms"),
                    antonyms = definitionJson.stringList("antonyms"),
                )
            }

            meanings += DictionaryMeaning(
                partOfSpeech = meaningJson.optionalString("partOfSpeech").orEmpty(),
                definitions = definitions,
                synonyms = meaningJson.stringList("synonyms"),
                antonyms = meaningJson.stringList("antonyms"),
            )
        }

        return DictionaryEntry(
            word = entry.optionalString("word").orEmpty(),
            phonetics = findPhonetics(entry),
            meanings = meanings,
            origin = entry.optionalString("origin"),
            sourceUrls = entry.stringList("sourceUrls"),
            license = entry.optJSONObject("license")?.let { licenseJson ->
                licenseJson.optionalString("name")?.let { name ->
                    DictionaryLicense(
                        name = name,
                        url = licenseJson.optionalString("url"),
                    )
                }
            },
        )
    }

    private fun findPhonetics(entry: JSONObject): List<String> = buildList {
        entry.optionalString("phonetic")?.let(::add)

        val phonetics = entry.optJSONArray("phonetics") ?: JSONArray()
        for (index in 0 until phonetics.length()) {
            phonetics.optJSONObject(index)
                ?.optionalString("text")
                ?.let(::add)
        }
    }.distinct()

    private fun JSONObject.optionalString(key: String): String? =
        optString(key)
            .trim()
            .takeIf { it.isNotEmpty() && it != "null" }

    private fun JSONObject.stringList(key: String): List<String> {
        val values = optJSONArray(key) ?: return emptyList()
        return buildList {
            for (index in 0 until values.length()) {
                values.optString(index)
                    .trim()
                    .takeIf { it.isNotEmpty() }
                    ?.let(::add)
            }
        }
    }
}
