package com.example.project1_438

import org.junit.Assert.assertEquals
import org.junit.Test

class DashboardApiTest {

    @Test
    fun dictionaryResponse_parsesAllWordInformation() {
        val response = """
            [
              {
                "word": "hello",
                "phonetic": "/həˈləʊ/",
                "origin": "Early 19th century.",
                "meanings": [
                  {
                    "partOfSpeech": "exclamation",
                    "definitions": [
                      {
                        "definition": "Used as a greeting.",
                        "example": "Hello there!",
                        "synonyms": ["hi"],
                        "antonyms": ["goodbye"]
                      }
                    ],
                    "synonyms": ["greeting"],
                    "antonyms": []
                  }
                ],
                "license": {
                  "name": "Example license",
                  "url": "https://example.com/license"
                },
                "sourceUrls": ["https://example.com/hello"]
              }
            ]
        """.trimIndent()

        val entry = DictionaryApi.parse(response)
        val meaning = entry.meanings.single()
        val definition = meaning.definitions.single()

        assertEquals("hello", entry.word)
        assertEquals(listOf("/həˈləʊ/"), entry.phonetics)
        assertEquals("Early 19th century.", entry.origin)
        assertEquals("exclamation", meaning.partOfSpeech)
        assertEquals(listOf("greeting"), meaning.synonyms)
        assertEquals("Used as a greeting.", definition.text)
        assertEquals("Hello there!", definition.example)
        assertEquals(listOf("hi"), definition.synonyms)
        assertEquals(listOf("goodbye"), definition.antonyms)
        assertEquals(listOf("https://example.com/hello"), entry.sourceUrls)
        assertEquals("Example license", entry.license?.name)
        assertEquals("https://example.com/license", entry.license?.url)
    }
}
