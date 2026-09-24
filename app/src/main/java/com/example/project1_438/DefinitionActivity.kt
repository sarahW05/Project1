package com.example.project1_438

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project1_438.ui.theme.Project1438Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefinitionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val word = intent.getStringExtra(EXTRA_WORD).orEmpty()

        setContent {
            Project1438Theme {
                DefinitionScreen(
                    word = word,
                    onHomeClick = { finish() },
                )
            }
        }
    }

    companion object {
        const val EXTRA_WORD = "word"
    }
}

private sealed interface DefinitionUiState {
    data object Loading : DefinitionUiState
    data class Loaded(val entry: DictionaryEntry) : DefinitionUiState
    data object Error : DefinitionUiState
}

@Composable
internal fun DefinitionScreen(
    word: String,
    onHomeClick: () -> Unit,
    entryLoader: (String) -> DictionaryEntry = { DictionaryRepository.lookUp(it) },
) {
    var uiState by remember(word) {
        mutableStateOf<DefinitionUiState>(DefinitionUiState.Loading)
    }

    LaunchedEffect(word) {
        uiState = runCatching {
            withContext(Dispatchers.IO) {
                entryLoader(word)
            }
        }.fold(
            onSuccess = { DefinitionUiState.Loaded(it) },
            onFailure = { error ->
                Log.e("DefinitionActivity", "Unable to load $word", error)
                DefinitionUiState.Error
            },
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .systemBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(top = 16.dp, bottom = 32.dp),
    ) {
        Button(onClick = onHomeClick) {
            Text(
                text = "Home",
                color = Color.Black,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = word,
            color = Color.Black,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
        )

        when (val state = uiState) {
            DefinitionUiState.Loading -> {
                Spacer(modifier = Modifier.height(24.dp))
                DefinitionText("Loading...")
            }

            DefinitionUiState.Error -> {
                Spacer(modifier = Modifier.height(24.dp))
                DefinitionText(DEFINITION_UNAVAILABLE)
            }

            is DefinitionUiState.Loaded -> {
                DefinitionContent(state.entry)
            }
        }
    }
}

@Composable
private fun DefinitionContent(entry: DictionaryEntry) {
    if (entry.phonetics.isNotEmpty()) {
        Spacer(modifier = Modifier.height(4.dp))
        DefinitionText(entry.phonetics.joinToString("  "))
    }

    Spacer(modifier = Modifier.height(24.dp))

    entry.meanings.forEach { meaning ->
        if (meaning.partOfSpeech.isNotEmpty()) {
            Text(
                text = meaning.partOfSpeech.uppercase(),
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        DefinitionText("Definitions:")
        Spacer(modifier = Modifier.height(8.dp))

        meaning.definitions.forEachIndexed { index, definition ->
            DefinitionText("${index + 1}. ${definition.text}")

            definition.example?.let {
                DefinitionText("Example: $it")
            }

            if (definition.synonyms.isNotEmpty()) {
                DefinitionText("Synonyms: ${definition.synonyms.joinToString()}")
            }

            if (definition.antonyms.isNotEmpty()) {
                DefinitionText("Antonyms: ${definition.antonyms.joinToString()}")
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        if (meaning.synonyms.isNotEmpty()) {
            DefinitionText("Synonyms: ${meaning.synonyms.joinToString()}")
        }

        if (meaning.antonyms.isNotEmpty()) {
            DefinitionText("Antonyms: ${meaning.antonyms.joinToString()}")
        }

        Spacer(modifier = Modifier.height(16.dp))
    }

    entry.origin?.let {
        DefinitionText("ORIGIN")
        DefinitionText(it)
        Spacer(modifier = Modifier.height(16.dp))
    }

    if (entry.sourceUrls.isNotEmpty()) {
        DefinitionText("SOURCES")
        val uriHandler = LocalUriHandler.current

        entry.sourceUrls.forEach { sourceUrl ->
            Text(
                text = sourceUrl,
                color = Color.Black,
                fontSize = 18.sp,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { uriHandler.openUri(sourceUrl) },
            )
        }
    }
}

@Composable
private fun DefinitionText(text: String) {
    Text(
        text = text,
        color = Color.Black,
        fontSize = 18.sp,
        modifier = Modifier.fillMaxWidth(),
    )
}
