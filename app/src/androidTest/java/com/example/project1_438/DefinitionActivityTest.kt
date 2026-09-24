package com.example.project1_438

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class DefinitionActivityTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun enteredWordTest() {
        assertWordIsDisplayed("apple")
    }

    @Test
    fun bananaTest() {
        assertWordIsDisplayed("banana")
    }

    @Test
    fun dictionaryTest() {
        assertWordIsDisplayed("dictionary")
    }

    @Test
    fun capitalizedWordTest() {
        assertWordIsDisplayed("Hello")
    }

    @Test
    fun homeButton_callsHomeAction() {
        var homeClicked = false

        showDefinition(
            word = "apple",
            onHomeClick = { homeClicked = true },
        )

        composeTestRule
            .onNodeWithText("Home")
            .performClick()

        assertTrue(homeClicked)
    }

    private fun assertWordIsDisplayed(word: String) {
        showDefinition(word)

        composeTestRule
            .onNodeWithText(word)
            .assertExists()
    }

    private fun showDefinition(
        word: String,
        onHomeClick: () -> Unit = {},
    ) {
        composeTestRule.setContent {
            DefinitionScreen(
                word = word,
                onHomeClick = onHomeClick,
                entryLoader = { EMPTY_ENTRY },
            )
        }
    }

    private companion object {
        val EMPTY_ENTRY = DictionaryEntry(
            word = "",
            phonetics = emptyList(),
            meanings = emptyList(),
            origin = null,
            sourceUrls = emptyList(),
        )
    }
}
