package com.example.project1_438

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.project1_438.database.SessionManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SessionManagerTest {

    private lateinit var sessionManager: SessionManager

    /*
     * Creates the SessionManager and clears any value left by a previous test.
     */
    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        sessionManager = SessionManager(context)

        runTest {
            sessionManager.clearUserId()
        }
    }

    /*
     * Clears the stored user ID after each test so tests do not affect
     * one another.
     */
    @After
    fun tearDown() {
        runTest {
            sessionManager.clearUserId()
        }
    }

    /*
     * Verifies that a user ID can be saved and read from DataStore.
     */
    @Test
    fun saveUserId_storesAndReturnsUserId() = runTest {
        val userId = 15L

        sessionManager.saveUserId(userId)

        val storedUserId = sessionManager.userIdFlow().first()

        Assert.assertEquals(userId, storedUserId)
    }

    /*
     * Verifies that clearing the session removes the saved user ID.
     */
    @Test
    fun clearUserId_removesStoredUserId() = runTest {
        sessionManager.saveUserId(15L)

        sessionManager.clearUserId()

        val storedUserId = sessionManager.userIdFlow().first()

        Assert.assertNull(storedUserId)
    }

    /*
     * Verifies that saving a new user ID replaces the previous one.
     */
    @Test
    fun saveUserId_replacesPreviousUserId() = runTest {
        sessionManager.saveUserId(15L)
        sessionManager.saveUserId(27L)

        val storedUserId = sessionManager.userIdFlow().first()

        Assert.assertEquals(27L, storedUserId)
    }
}
