package com.example.project1_438

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.URL

internal const val DASHBOARD_WORD = "hello"
internal const val DEFINITION_UNAVAILABLE = "Definition unavailable"

private const val DICTIONARY_API_BASE_URL =
    "https://api.dictionaryapi.dev/api/v2/entries/en"

internal fun fetchDefinition(
    word: String,
    responseLoader: (String) -> String = { url -> URL(url).readText() }
): String {
    return try {
        val response = responseLoader("$DICTIONARY_API_BASE_URL/$word")

        JSONArray(response)
            .getJSONObject(0)
            .getJSONArray("meanings")
            .getJSONObject(0)
            .getJSONArray("definitions")
            .getJSONObject(0)
            .getString("definition")
    } catch (e: Exception) {
        DEFINITION_UNAVAILABLE
    }
}

@Composable
fun DashboardScreen() {

    val word = DASHBOARD_WORD
    var definition by remember { mutableStateOf("Loading...") }

    LaunchedEffect(Unit) {
        definition = withContext(Dispatchers.IO) {
            fetchDefinition(word)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Row(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            //button for user page
            IconButton(onClick = {//input logic
             }) {
                //person icon
                Text("\uD83D\uDC64")
            }

            //login button
            Button(
                onClick = {
                    //logic
                }) {
                Text("Log In")
            }
        }

        Spacer(
            modifier = Modifier.height(160.dp)
        )

        //word from api??
        Text(text = word)
        Text(text = definition)

        Spacer(
            modifier = Modifier.height(200.dp)
        )

        //search bar
        OutlinedTextField(
            value = "",
            onValueChange = {
                //input logic
            },
            label = {
                Text("Search up a word")
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
