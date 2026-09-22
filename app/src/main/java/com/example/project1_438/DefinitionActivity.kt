package com.example.project1_438

import android.os.Bundle
import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DefinitionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_definition)
        val word = intent.getStringExtra(EXTRA_WORD).orEmpty()
        val phoneticText = findViewById<TextView>(R.id.phoneticText)
        val definitionText = findViewById<TextView>(R.id.definitionText)

        findViewById<Button>(R.id.homeButton).setOnClickListener { finish() }
        findViewById<TextView>(R.id.wordText).text = word
        definitionText.text = "Loading..."

        lifecycleScope.launch {
            runCatching {
                withContext(Dispatchers.IO) {
                    DictionaryApi.lookUp(word)
                }
            }.onSuccess { entry ->
                phoneticText.text = entry.phonetics.joinToString("  ")
                phoneticText.visibility =
                    if (entry.phonetics.isEmpty()) View.GONE else View.VISIBLE
                definitionText.text = formatEntry(entry)
            }.onFailure { error ->
                Log.e("DefinitionActivity", "Unable to load $word", error)
                phoneticText.visibility = View.GONE
                definitionText.text = DEFINITION_UNAVAILABLE
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun formatEntry(entry: DictionaryEntry): CharSequence {
        val result = SpannableStringBuilder()

        entry.meanings.forEach { meaning ->
            if (meaning.partOfSpeech.isNotEmpty()) {
                val headingStart = result.length
                result.append(meaning.partOfSpeech.uppercase())
                result.setSpan(
                    StyleSpan(Typeface.BOLD),
                    headingStart,
                    result.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                result.append("\n")
            }
            result.append("Definitions:\n\n")

            meaning.definitions.forEachIndexed { index, definition ->
                result.append("${index + 1}. ${definition.text}\n")
                definition.example?.let { result.append("Example: $it\n") }

                if (definition.synonyms.isNotEmpty()) {
                    result.append("Synonyms: ${definition.synonyms.joinToString()}\n")
                }
                if (definition.antonyms.isNotEmpty()) {
                    result.append("Antonyms: ${definition.antonyms.joinToString()}\n")
                }
                result.append("\n")
            }

            if (meaning.synonyms.isNotEmpty()) {
                result.append("Synonyms: ${meaning.synonyms.joinToString()}\n")
            }
            if (meaning.antonyms.isNotEmpty()) {
                result.append("Antonyms: ${meaning.antonyms.joinToString()}\n")
            }
            if (meaning.synonyms.isNotEmpty() || meaning.antonyms.isNotEmpty()) {
                result.append("\n")
            }
        }

        entry.origin?.let {
            result.append("ORIGIN\n$it\n\n")
        }

        if (entry.sourceUrls.isNotEmpty()) {
            result.append("SOURCES\n${entry.sourceUrls.joinToString("\n")}\n\n")
        }

        entry.license?.let { license ->
            result.append("LICENSE\n${license.name}")
            license.url?.let { result.append("\n$it") }
        }

        while (result.isNotEmpty() && result.last().isWhitespace()) {
            result.delete(result.length - 1, result.length)
        }

        return result.ifEmpty { DEFINITION_UNAVAILABLE }
    }

    companion object {
        const val EXTRA_WORD = "word"
    }
}
