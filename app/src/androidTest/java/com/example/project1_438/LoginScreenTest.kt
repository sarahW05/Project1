package com.example.project1_438

import android.content.Context
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.room3.Room
import androidx.sqlite.driver.AndroidSQLiteDriver
import androidx.test.core.app.ApplicationProvider
import com.example.project1_438.database.AppDatabase
import com.example.project1_438.database.User
import com.example.project1_438.database.UserDao
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var database: AppDatabase
    private lateinit var userDao: UserDao

    // Creates a temporary database before each test
    @Before
    fun createDatabase() {
        val context =
            ApplicationProvider.getApplicationContext<Context>()

        database =
            Room.inMemoryDatabaseBuilder<AppDatabase>(context)
                .setDriver(AndroidSQLiteDriver())
                .build()

        userDao = database.userDAO()
    }

    // Closes the temporary database after each test
    @After
    fun closeDatabase() {
        database.close()
    }

    /*
     * Test 1:
     * Verifies that the important parts of the
     * login screen are displayed.
     */
    @Test
    fun loginScreen_displaysCorrectly() {

        composeTestRule.setContent {
            LoginScreen(
                userDao = userDao,
                onLoginSuccess = {}
            )
        }

        composeTestRule
            .onNodeWithText("YourDictionary")
            .assertExists()

        composeTestRule
            .onNodeWithText("Username")
            .assertExists()

        composeTestRule
            .onNodeWithText("Password")
            .assertExists()

        composeTestRule
            .onNode(
                hasText("Log In") and hasClickAction()
            )
            .assertExists()
    }

    /*
     * Test 2:
     * Verifies that the username and password
     * text fields accept input.
     */
    @Test
    fun loginScreen_acceptsUsernameAndPassword() {

        composeTestRule.setContent {
            LoginScreen(
                userDao = userDao,
                onLoginSuccess = {}
            )
        }

        composeTestRule
            .onNodeWithText("Username")
            .performTextInput("testUser")

        composeTestRule
            .onNodeWithText("Password")
            .performTextInput("password")

        composeTestRule
            .onNodeWithText("Username")
            .assertTextContains("testUser")

        composeTestRule
            .onNodeWithText("Password")
            .assertTextContains("password")
    }

    /*
     * Test 3:
     * Verifies that incorrect login information
     * displays an error message.
     */
    @Test
    fun incorrectLogin_displaysErrorMessage() {

        composeTestRule.setContent {
            LoginScreen(
                userDao = userDao,
                onLoginSuccess = {}
            )
        }

        composeTestRule
            .onNodeWithText("Username")
            .performTextInput("wrongUser")

        composeTestRule
            .onNodeWithText("Password")
            .performTextInput("wrongPassword")

        composeTestRule
            .onNode(
                hasText("Log In") and hasClickAction()
            )
            .performClick()

        composeTestRule
            .onNodeWithText("Incorrect username or password")
            .assertExists()
    }

    /*
     * Test 4:
     * Verifies that correct login information
     * calls onLoginSuccess.
     */
    @Test
    fun correctLogin_callsOnLoginSuccess() = runTest {

        val user = User(
            userName = "testUser",
            firstName = "Test",
            lastName = "User",
            password = "password"
        )

        userDao.insertUser(user)

        var loginSuccessful = false

        composeTestRule.setContent {
            LoginScreen(
                userDao = userDao,
                onLoginSuccess = {
                    loginSuccessful = true
                }
            )
        }

        composeTestRule
            .onNodeWithText("Username")
            .performTextInput("testUser")

        composeTestRule
            .onNodeWithText("Password")
            .performTextInput("password")

        composeTestRule
            .onNode(
                hasText("Log In") and hasClickAction()
            )
            .performClick()

        composeTestRule.waitUntil(
            timeoutMillis = 5000
        ) {
            loginSuccessful
        }

        assertTrue(loginSuccessful)
    }
}