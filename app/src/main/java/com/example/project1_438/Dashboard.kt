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
import com.example.project1_438.database.User
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal const val DASHBOARD_WORD = "hello"
internal const val DEFINITION_UNAVAILABLE = "Definition unavailable"

internal fun fetchDefinition(word: String): String {
    return try {
        DictionaryRepository.lookUp(word).firstDefinition() ?: DEFINITION_UNAVAILABLE
    } catch (e: Exception) {
        DEFINITION_UNAVAILABLE
    }
}

@Composable
fun DashboardScreen(
    loggedInUserId: Long?,
    onLoginClick: () -> Unit,
    onUserClick: () -> Unit,
    onSearch: (String) -> Unit,
) {

    val word = DASHBOARD_WORD
    var definition by remember { mutableStateOf("Loading...") }
    var searchWord by rememberSaveable { mutableStateOf("") }

    fun openDefinitionPage() {
        if (searchWord.isNotBlank()) {
            onSearch(searchWord)
        }
    }

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
            IconButton(onClick = {
                onUserClick()
             }) {
                //person icon
                Text("\uD83D\uDC64")
            }

            //login button
            Button(
                onClick = onLoginClick
            ) {
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
            value = searchWord,
            onValueChange = { searchWord = it },
            label = {
                Text("Search up a word")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = { openDefinitionPage() }
            )
        )

        Button(
            onClick = { openDefinitionPage() },
            enabled = searchWord.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }
    }
}
