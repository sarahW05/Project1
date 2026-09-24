package com.example.project1_438

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.room3.Room
import androidx.sqlite.driver.AndroidSQLiteDriver
import androidx.test.core.app.ApplicationProvider
import com.example.project1_438.database.AppDatabase
import com.example.project1_438.database.User
import com.example.project1_438.database.UserDao
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserPageTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var database: AppDatabase
    private lateinit var userDao: UserDao

    private var userId: Long = 0


    // Runs before each test
    @Before
    fun createDatabase() = runTest {

        val context =
            ApplicationProvider.getApplicationContext<Context>()

        database =
            Room.inMemoryDatabaseBuilder<AppDatabase>(context)
                .setDriver(AndroidSQLiteDriver())
                .build()

        userDao = database.userDAO()

        // Create a test user
        val testUser = User(
            userName = "testUser",
            firstName = "User",
            lastName = "Test",
            password = "password"
        )

        // Insert user and save their ID
        userId = checkNotNull(userDao.insertUser(testUser))
    }


    // Runs after each test
    @After
    fun closeDatabase() {
        database.close()
    }


    /*
     * TEST 1
     * Makes sure information from the database
     * is displayed on the User Page.
     */
    @Test
    fun userPage_displaysUserInformation() {

        composeTestRule.setContent {

            UserPage(
                userId = userId,
                userDao = userDao
            )
        }

        // Check name
        composeTestRule
            .onNodeWithText("Name: User Test")
            .assertExists()

        // Check username
        composeTestRule
            .onNodeWithText("Username: testUser")
            .assertExists()

        // Check Edit Information button
        composeTestRule
            .onNodeWithText("Edit Information")
            .assertExists()
    }


    /*
     * TEST 2
     * Makes sure clicking Edit Information
     * opens the editable fields.
     */
    @Test
    fun editInformation_displaysEditableFields() {

        composeTestRule.setContent {

            UserPage(
                userId = userId,
                userDao = userDao
            )
        }

        // Click Edit Information
        composeTestRule
            .onNodeWithText("Edit Information")
            .performClick()

        // Check editable fields
        composeTestRule
            .onNodeWithText("First Name")
            .assertExists()

        composeTestRule
            .onNodeWithText("Last Name")
            .assertExists()

        composeTestRule
            .onNodeWithText("Username")
            .assertExists()

        composeTestRule
            .onNodeWithText("Password")
            .assertExists()

        // Check buttons
        composeTestRule
            .onNodeWithText("Save Changes")
            .assertExists()

        composeTestRule
            .onNodeWithText("Cancel")
            .assertExists()
    }


    /*
     * TEST 3
     * Makes sure Cancel does not save
     * the changed information.
     */
    @Test
    fun cancelEdit_doesNotChangeUser() {

        composeTestRule.setContent {

            UserPage(
                userId = userId,
                userDao = userDao
            )
        }

        // Open edit mode
        composeTestRule
            .onNodeWithText("Edit Information")
            .performClick()

        // Clear original first name
        composeTestRule
            .onNodeWithText("First Name")
            .performTextClearance()

        // Enter a different first name
        composeTestRule
            .onNodeWithText("First Name")
            .performTextInput("Sarah")

        // Cancel
        composeTestRule
            .onNodeWithText("Cancel")
            .performClick()

        // Original information should still be displayed
        composeTestRule
            .onNodeWithText("Name: User Test")
            .assertExists()

        composeTestRule
            .onNodeWithText("Username: testUser")
            .assertExists()
    }


    /*
     * TEST 4
     * Makes sure Save Changes updates
     * the user's information.
     */
    @Test
    fun saveChanges_updatesUserInformation() = runTest {

        composeTestRule.setContent {

            UserPage(
                userId = userId,
                userDao = userDao
            )
        }

        // Open edit mode
        composeTestRule
            .onNodeWithText("Edit Information")
            .performClick()


        // Change first name
        composeTestRule
            .onNodeWithText("First Name")
            .performTextClearance()

        composeTestRule
            .onNodeWithText("First Name")
            .performTextInput("Sarah")


        // Change last name
        composeTestRule
            .onNodeWithText("Last Name")
            .performTextClearance()

        composeTestRule
            .onNodeWithText("Last Name")
            .performTextInput("Wafa")


        // Change username
        composeTestRule
            .onNodeWithText("Username")
            .performTextClearance()

        composeTestRule
            .onNodeWithText("Username")
            .performTextInput("sarah123")


        // Save
        composeTestRule
            .onNodeWithText("Save Changes")
            .performClick()

        // Check that the new information is displayed
        composeTestRule
            .onNodeWithText("Name: Sarah Wafa")
            .assertExists()

        composeTestRule
            .onNodeWithText("Username: sarah123")
            .assertExists()


        // Check the actual database
        val updatedUser =
            userDao.getUserById(userId)

        assertEquals(
            "Sarah",
            updatedUser?.firstName
        )

        assertEquals(
            "Wafa",
            updatedUser?.lastName
        )

        assertEquals(
            "sarah123",
            updatedUser?.userName
        )
    }
}
