//Regenerated with Mr. GPT to reflect changes to some database files and objects
package com.example.project1_438

import android.content.Context
import androidx.room3.Room
import androidx.sqlite.driver.AndroidSQLiteDriver
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.project1_438.database.AppDatabase
import com.example.project1_438.database.Favorite
import com.example.project1_438.database.FavoriteDao
import com.example.project1_438.database.User
import com.example.project1_438.database.UserDao
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

// This test class checks the Room database without using the app's
// permanently stored database.
@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {

    // These variables hold the temporary database and its DAO objects.
    // late init means they will be assigned before each test runs.
    private lateinit var database: AppDatabase
    private lateinit var userDao: UserDao
    private lateinit var favoriteDao: FavoriteDao

    /*
     * Creates a new temporary database before each test.
     *
     * Using an in-memory database means every test starts with empty tables
     * and cannot change the app's actual stored database.
     */
    @Before
    fun createDatabase() {
        // ApplicationProvider supplies the Android Context required to create
        // the Room database during an instrumented test.
        val context = ApplicationProvider.getApplicationContext<Context>()

        // This creates the temporary database in memory.
        database = Room.inMemoryDatabaseBuilder<AppDatabase>(context)
            .setDriver(AndroidSQLiteDriver())
            .build()

        // These assignments give the tests direct access to the DAO methods.
        userDao = database.userDAO()
        favoriteDao = database.favoritesDAO()
    }

    /*
     * Closes the temporary database after every test.
     *
     * Closing it releases its resources and prevents one test from leaving
     * an open database connection for the next test.
     */
    @After
    fun closeDatabase() {
        database.close()
    }

    /*
     * Verifies that a favorite can be inserted and retrieved for the
     * correct user.
     */
    @Test
    fun insertFavorite_retrievesFavoriteForCorrectUser() = runTest {
        // This User object represents the account that owns the favorite.
        val user = User(
            userId = 1L,
            userName = "testUser",
            firstName = "Test",
            lastName = "User",
            password = "testPassword"
        )

        // This Favorite object represents one word saved by that user.
        val favorite = Favorite(
            userId = 1L,
            word = "kotlin"
        )

        // The user must exist before the favorite can satisfy its foreign key.
        userDao.insertUser(user)

        // Insert the favorite into the Favorite table.
        favoriteDao.insertFavorite(favorite)

        // Retrieve only the favorites belonging to user ID 1.
        val retrievedFavorites =
            favoriteDao.getUserFavoritesById(1L)

        // The returned list should contain the favorite that was inserted.
        assertEquals(listOf(favorite), retrievedFavorites)
    }

    /*
     * Verifies that a user can be found by username.
     */
    @Test
    fun getUserByUserName_returnsMatchingUser() = runTest {
        // userId remains 0 so Room can generate the ID automatically.
        val user = User(
            userName = "testUser",
            firstName = "Test",
            lastName = "User",
            password = "password"
        )

        // Room returns the generated database ID after insertion.
        val insertedId = userDao.insertUser(user)

        // A successful insert should provide a generated ID.
        assertNotNull(insertedId)

        // Search the database using the username.
        val actualUser =
            userDao.getUserByUserName("testUser")

        // Confirm that a matching user was found.
        assertNotNull(actualUser)

        // Confirm that the returned user has the generated database ID.
        assertEquals(insertedId, actualUser?.userId)

        // Confirm that the username returned by the query is correct.
        assertEquals("testUser", actualUser?.userName)
    }

    /*
     * Verifies that searching for a username that does not exist
     * returns null.
     */
    @Test
    fun getUserByUserName_returnsNullWhenUserDoesNotExist() = runTest {
        // No user with this username has been inserted into the database.
        val actualUser =
            userDao.getUserByUserName("missingUser")

        // The DAO should return null when no matching row exists.
        assertNull(actualUser)
    }

    /*
     * Verifies that login returns the matching user when both the
     * username and password are correct.
     */
    @Test
    fun login_returnsMatchingUserWithCorrectCredentials() = runTest {
        // This is the user whose credentials will be tested.
        val user = User(
            userId = 1L,
            userName = "testUser",
            firstName = "Test",
            lastName = "User",
            password = "password"
        )

        // Insert the user before attempting to log in.
        userDao.insertUser(user)

        // The login query searches for both values at the same time.
        val loggedInUser =
            userDao.login("testUser", "password")

        // The returned user should match the inserted user.
        assertEquals(user, loggedInUser)
    }

    /*
     * Verifies that login returns null when the password is incorrect.
     */
    @Test
    fun login_returnsNullWithIncorrectPassword() = runTest {
        // Store a user with the expected password.
        val user = User(
            userId = 1L,
            userName = "testUser",
            firstName = "Test",
            lastName = "User",
            password = "password"
        )

        userDao.insertUser(user)

        // The username exists, but the supplied password is wrong.
        val loggedInUser =
            userDao.login("testUser", "wrongPassword")

        // No user should be returned for incorrect credentials.
        assertNull(loggedInUser)
    }

    /*
     * Verifies that querying one user does not return another user's
     * favorites.
     */
    @Test
    fun getUserFavorites_returnsOnlyFavoritesBelongingToRequestedUser() =
        runTest {
            // Create two separate users with different IDs.
            val firstUser = User(
                userId = 1L,
                userName = "firstUser",
                firstName = "First",
                lastName = "User",
                password = "password"
            )

            val secondUser = User(
                userId = 2L,
                userName = "secondUser",
                firstName = "Second",
                lastName = "User",
                password = "password"
            )

            // Assign one different favorite to each user.
            val firstUserFavorite = Favorite(
                userId = 1L,
                word = "kotlin"
            )

            val secondUserFavorite = Favorite(
                userId = 2L,
                word = "android"
            )

            // Insert both users before inserting their favorites.
            userDao.insertUsers(listOf(firstUser, secondUser))

            // Insert both favorites in one DAO operation.
            favoriteDao.insertFavorites(
                listOf(firstUserFavorite, secondUserFavorite)
            )

            // Ask only for the favorites belonging to the first user.
            val firstUserFavorites =
                favoriteDao.getUserFavoritesById(1L)

            // The second user's favorite must not appear in the result.
            assertEquals(
                listOf(firstUserFavorite),
                firstUserFavorites
            )
        }

    /*
     * Verifies that the composite primary key prevents the same user
     * from favoriting the same word more than once.
     *
     * FavoriteDao uses OnConflictStrategy.IGNORE, so the second insert
     * is ignored instead of creating a duplicate row.
     */
    @Test
    fun insertDuplicateFavorite_keepsOnlyOneCopy() = runTest {
        // Create the user who will own the favorite.
        val user = User(
            userId = 1L,
            userName = "testUser",
            firstName = "Test",
            lastName = "User",
            password = "password"
        )

        // The combination of userId and word identifies this favorite.
        val favorite = Favorite(
            userId = 1L,
            word = "kotlin"
        )

        userDao.insertUser(user)

        // Insert the same favorite twice.
        favoriteDao.insertFavorite(favorite)
        favoriteDao.insertFavorite(favorite)

        // Retrieve all favorites belonging to that user.
        val retrievedFavorites =
            favoriteDao.getUserFavoritesById(1L)

        // Only one copy should remain after the duplicate insert.
        assertEquals(1, retrievedFavorites.size)

        // Confirm that the remaining row is the expected favorite.
        assertEquals(favorite, retrievedFavorites.single())
    }

    /*
     * Verifies that a favorite can be deleted.
     */
    @Test
    fun deleteFavorite_removesFavorite() = runTest {
        // Create the user who owns the favorite.
        val user = User(
            userId = 1L,
            userName = "testUser",
            firstName = "Test",
            lastName = "User",
            password = "password"
        )

        val favorite = Favorite(
            userId = 1L,
            word = "kotlin"
        )

        // Insert the required parent row and child row.
        userDao.insertUser(user)
        favoriteDao.insertFavorite(favorite)

        // Delete the favorite row.
        favoriteDao.deleteFavorite(favorite)

        // Query the user's favorites after deletion.
        val retrievedFavorites =
            favoriteDao.getUserFavoritesById(1L)

        // The list should contain no favorites.
        assertTrue(retrievedFavorites.isEmpty())
    }

    /*
     * Verifies the foreign-key cascade rule.
     *
     * When a user is deleted, all favorites belonging to that user
     * should be deleted automatically.
     */
    @Test
    fun deleteUser_deletesUsersFavorites() = runTest {
        // Create a user and one favorite belonging to that user.
        val user = User(
            userId = 1L,
            userName = "testUser",
            firstName = "Test",
            lastName = "User",
            password = "password"
        )

        val favorite = Favorite(
            userId = 1L,
            word = "kotlin"
        )

        // Insert the user first because Favorite has a foreign key
        // referencing the User table.
        userDao.insertUser(user)
        favoriteDao.insertFavorite(favorite)

        // The Favorite entity uses CASCADE for user deletion.
        userDao.deleteUser(user)

        // Query for favorites after deleting the parent user.
        val retrievedFavorites =
            favoriteDao.getUserFavoritesById(1L)

        // The cascade rule should have removed the favorite automatically.
        assertTrue(retrievedFavorites.isEmpty())
    }
}