//Tests generated with GPT
package com.example.project1_438

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.room3.Room
import androidx.sqlite.driver.AndroidSQLiteDriver
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.project1_438.database.AppDatabase
import com.example.project1_438.database.SessionManager
import com.example.project1_438.database.User
import com.example.project1_438.database.UserDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.atomic.AtomicBoolean

@RunWith(AndroidJUnit4::class)
class CreateAccountTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var database: AppDatabase
    private lateinit var userDao: UserDao
    private lateinit var sessionManager: SessionManager

    /*
     * Creates a temporary in-memory database before each test.
     *
     * The in-memory database ensures that the tests do not affect the
     * application's permanently stored database.
     */
    @Before
    fun createDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder<AppDatabase>(context)
            .setDriver(AndroidSQLiteDriver())
            .build()

        // These references allow the tests to call the database functions.
        userDao = database.userDAO()
        sessionManager = SessionManager(context)

        // Ensures that each test begins without a saved login session.
        runTest {
            sessionManager.clearUserId()
        }
    }

    /*
     * Closes the temporary database and clears the saved login ID after
     * each test has finished.
     */
    @After
    fun closeDatabase() {
        runTest {
            sessionManager.clearUserId()
        }

        database.close()
    }

    /*
     * Enters the supplied values into the account-creation form.
     *
     * An empty string intentionally leaves a field blank so that individual
     * blank-field validation cases can be tested.
     */
    private fun enterAccountInformation(
        username: String,
        firstName: String,
        lastName: String,
        password: String,
        confirmation: String
    ) {
        composeTestRule
            .onNodeWithText("Username")
            .performTextInput(username)

        composeTestRule
            .onNodeWithText("First Name")
            .performTextInput(firstName)

        composeTestRule
            .onNodeWithText("Last Name")
            .performTextInput(lastName)

        composeTestRule
            .onNodeWithText("Password")
            .performTextInput(password)

        composeTestRule
            .onNodeWithText("Confirm Password")
            .performTextInput(confirmation)
    }

    /*
     * Displays the CreateAccount composable using the temporary database
     * and session manager created for the current test.
     */
    private fun displayCreateAccount(
        onAccountCreated: () -> Unit = {}
    ) {
        composeTestRule.setContent {
            CreateAccount(
                userDao = userDao,
                sessionManager = sessionManager,
                onAccCreated = onAccountCreated
            )
        }
    }

    /*
     * Waits for the blank-field validation message to appear.
     *
     * The account-creation logic runs inside a coroutine, so the test waits
     * for the composable to display the updated error message.
     */
    private fun assertRequiredFieldsErrorDisplayed() {
        composeTestRule.waitUntil(5_000) {
            composeTestRule
                .onAllNodesWithText(
                    "All Fields Required for Account Creation."
                )
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        composeTestRule
            .onNodeWithText("All Fields Required for Account Creation.")
            .assertIsDisplayed()
    }

    /*
     * Verifies that submitting the completely empty form is rejected.
     */
    @Test
    fun allFieldsBlank_displaysRequiredFieldsError() = runTest {
        displayCreateAccount()

        // Clicking the button submits the form while every field is empty.
        composeTestRule
            .onNodeWithText("Create Account")
            .performClick()

        // The blank-field error should be displayed.
        assertRequiredFieldsErrorDisplayed()

        // No user should be inserted when all fields are blank.
        assertEquals(0, userDao.getAllUsers().size)
    }

    /*
     * Verifies that an empty username is rejected even when the other
     * fields contain valid values.
     */
    @Test
    fun usernameBlank_displaysRequiredFieldsError() = runTest {
        displayCreateAccount()

        enterAccountInformation(
            username = "",
            firstName = "New",
            lastName = "User",
            password = "password",
            confirmation = "password"
        )

        composeTestRule
            .onNodeWithText("Create Account")
            .performClick()

        assertRequiredFieldsErrorDisplayed()

        // The invalid submission must not create a database row.
        assertEquals(0, userDao.getAllUsers().size)
    }

    /*
     * Verifies that an empty first name is rejected.
     */
    @Test
    fun firstNameBlank_displaysRequiredFieldsError() = runTest {
        displayCreateAccount()

        enterAccountInformation(
            username = "newUser",
            firstName = "",
            lastName = "User",
            password = "password",
            confirmation = "password"
        )

        composeTestRule
            .onNodeWithText("Create Account")
            .performClick()

        assertRequiredFieldsErrorDisplayed()
        assertEquals(0, userDao.getAllUsers().size)
    }

    /*
     * Verifies that an empty last name is rejected.
     */
    @Test
    fun lastNameBlank_displaysRequiredFieldsError() = runTest {
        displayCreateAccount()

        enterAccountInformation(
            username = "newUser",
            firstName = "New",
            lastName = "",
            password = "password",
            confirmation = "password"
        )

        composeTestRule
            .onNodeWithText("Create Account")
            .performClick()

        assertRequiredFieldsErrorDisplayed()
        assertEquals(0, userDao.getAllUsers().size)
    }

    /*
     * Verifies that an empty password is rejected.
     */
    @Test
    fun passwordBlank_displaysRequiredFieldsError() = runTest {
        displayCreateAccount()

        enterAccountInformation(
            username = "newUser",
            firstName = "New",
            lastName = "User",
            password = "",
            confirmation = "password"
        )

        composeTestRule
            .onNodeWithText("Create Account")
            .performClick()

        assertRequiredFieldsErrorDisplayed()
        assertEquals(0, userDao.getAllUsers().size)
    }

    /*
     * Verifies that an empty password-confirmation field is rejected.
     */
    @Test
    fun passwordConfirmationBlank_displaysRequiredFieldsError() = runTest {
        displayCreateAccount()

        enterAccountInformation(
            username = "newUser",
            firstName = "New",
            lastName = "User",
            password = "password",
            confirmation = ""
        )

        composeTestRule
            .onNodeWithText("Create Account")
            .performClick()

        assertRequiredFieldsErrorDisplayed()
        assertEquals(0, userDao.getAllUsers().size)
    }

    /*
     * Verifies that the account is not created when the two password
     * fields contain different values.
     */
    @Test
    fun mismatchedPasswords_displaysErrorAndDoesNotCreateUser() = runTest {
        displayCreateAccount()

        enterAccountInformation(
            username = "newUser",
            firstName = "New",
            lastName = "User",
            password = "password1",
            confirmation = "password2"
        )

        composeTestRule
            .onNodeWithText("Create Account")
            .performClick()

        composeTestRule.waitUntil(5_000) {
            composeTestRule
                .onAllNodesWithText("Passwords must match")
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        composeTestRule
            .onNodeWithText("Passwords must match")
            .assertIsDisplayed()

        // The mismatched passwords should prevent account creation.
        assertNull(userDao.getUserByUserName("newUser"))
    }

    /*
     * Verifies that an existing username cannot be used to create another
     * account.
     */
    @Test
    fun existingUsername_displaysErrorAndDoesNotCreateDuplicateUser() =
        runTest {
            // Insert an account before displaying the creation screen.
            userDao.insertUser(
                User(
                    userName = "existingUser",
                    firstName = "Existing",
                    lastName = "User",
                    password = "password"
                )
            )

            displayCreateAccount()

            enterAccountInformation(
                username = "existingUser",
                firstName = "Another",
                lastName = "User",
                password = "password",
                confirmation = "password"
            )

            composeTestRule
                .onNodeWithText("Create Account")
                .performClick()

            composeTestRule.waitUntil(5_000) {
                composeTestRule
                    .onAllNodesWithText("Username already in use")
                    .fetchSemanticsNodes()
                    .isNotEmpty()
            }

            composeTestRule
                .onNodeWithText("Username already in use")
                .assertIsDisplayed()

            // Only the original account should exist for this username.
            val users = userDao.getAllUsers()
                .filter { it.userName == "existingUser" }

            assertEquals(1, users.size)
        }

    /*
     * Verifies that valid account information creates a database user,
     * saves the generated ID, and calls the account-created callback.
     */
    @Test
    fun validInformation_createsUserSavesSessionAndCallsCallback() =
        runTest {
            // Records whether the composable called the success callback.
            val accountCreated = AtomicBoolean(false)

            displayCreateAccount {
                accountCreated.set(true)
            }

            enterAccountInformation(
                username = "newUser",
                firstName = "New",
                lastName = "User",
                password = "password",
                confirmation = "password"
            )

            composeTestRule
                .onNodeWithText("Create Account")
                .performClick()

            // Waits for insertion, session saving, and callback execution.
            composeTestRule.waitUntil(5_000) {
                accountCreated.get()
            }

            // Retrieve the newly created account from Room.
            val createdUser =
                userDao.getUserByUserName("newUser")

            // A valid submission should create a user.
            requireNotNull(createdUser)

            // Confirm that the entered information was stored.
            assertEquals("newUser", createdUser.userName)
            assertEquals("New", createdUser.firstName)
            assertEquals("User", createdUser.lastName)
            assertEquals("password", createdUser.password)

            // The saved session ID should match the created user's ID.
            val savedUserId =
                sessionManager.userIdFlow().first()

            assertEquals(createdUser.userId, savedUserId)
        }
}