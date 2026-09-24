package com.example.project1_438

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.lifecycle.Lifecycle
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DefinitionActivityTest {

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

    private fun assertWordIsDisplayed(word: String) {
        launchDefinitionPage(word).use {
            onView(withId(R.id.wordText))
                .check(matches(withText(word)))
        }
    }

    private fun launchDefinitionPage(word: String): ActivityScenario<DefinitionActivity> {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val intent = Intent(context, DefinitionActivity::class.java).apply {
            putExtra(DefinitionActivity.EXTRA_WORD, word)
        }

        return ActivityScenario.launch(intent)
    }
}
