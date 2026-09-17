package com.example.project1_438

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.project1_438.ui.theme.Project1438Theme
import dev.jeziellago.compose.markdowntext.MarkdownText

class DefinitionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Project1438Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Text("""
                        # Word
                        **Noun**
                        Example Definition
                        **Verb**
                        Example Definition
                    """.trimIndent())
                }
            }
        }
    }
}
