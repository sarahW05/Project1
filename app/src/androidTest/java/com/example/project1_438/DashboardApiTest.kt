package com.example.project1_438

import org.json.JSONArray
import org.junit.Assert
import org.junit.Test
import java.net.URL

class DashboardApiTest {

    @Test
    fun dashboardContent_matchesDictionaryApiResponse() {
        Assert.assertEquals("hello", DASHBOARD_WORD)

        val apiResponse = URL(
            "https://api.dictionaryapi.dev/api/v2/entries/en/$DASHBOARD_WORD"
        ).readText()
        val apiDefinition = JSONArray(apiResponse)
            .getJSONObject(0)
            .getJSONArray("meanings")
            .getJSONObject(0)
            .getJSONArray("definitions")
            .getJSONObject(0)
            .getString("definition")

        Assert.assertTrue(apiDefinition.isNotBlank())
        Assert.assertEquals(apiDefinition, fetchDefinition(DASHBOARD_WORD))
    }
}