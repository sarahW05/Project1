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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

//This entire test case classes contents were generated, because it would've doubled dev time for a super simple implementation

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {

    private lateinit var database: AppDatabase
    private lateinit var userDao: UserDao
    private lateinit var favoriteDao: FavoriteDao

    /*
     * Creates a new temporary database before each test.
     *
     * This database exists only in memory, so every test starts with an
     * empty database and cannot affect the app's real stored database.
     */
    @Before
    fun createDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder<AppDatabase>(context)
            .setDriver(AndroidSQLiteDriver())
            .build()

        userDao = database.userDAO()
        favoriteDao = database.favoritesDAO()
    }

    /*
     * Closes the temporary database after each test so that the test does
     * not leave database connections open.
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
        val user = User(
            userId = 1L,
            userName = "testUser",
            firstName = "Test",
            lastName = "User",
            password = "testPassword"
        )

        val favorite = Favorite(
            userId = 1L,
            word = "kotlin"
        )

        userDao.insertUser(user)
        favoriteDao.insertFavorite(favorite)

        val retrievedFavorites =
            favoriteDao.getUserFavoritesById(userId = 1L)

        assertEquals(listOf(favorite), retrievedFavorites)
    }

    /*
     * Verifies that querying one user does not return another user's
     * favorites.
     */
    @Test
    fun getUserFavorites_returnsOnlyFavoritesBelongingToRequestedUser() =
        runTest {
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

            val firstUserFavorite = Favorite(
                userId = 1L,
                word = "kotlin"
            )

            val secondUserFavorite = Favorite(
                userId = 2L,
                word = "android"
            )

            userDao.insertUsers(listOf(firstUser, secondUser))
            favoriteDao.insertFavorites(
                listOf(firstUserFavorite, secondUserFavorite)
            )

            val firstUserFavorites =
                favoriteDao.getUserFavoritesById(userId = 1L)

            assertEquals(
                listOf(firstUserFavorite),
                firstUserFavorites
            )
        }

    /*
     * Verifies that the composite primary key prevents the same user from
     * favoriting the same word more than once.
     *
     * FavoriteDao uses OnConflictStrategy.IGNORE, so the duplicate insert
     * is ignored instead of causing an error.
     */
    @Test
    fun insertDuplicateFavorite_keepsOnlyOneCopy() = runTest {
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

        userDao.insertUser(user)

        favoriteDao.insertFavorite(favorite)
        favoriteDao.insertFavorite(favorite)

        val retrievedFavorites =
            favoriteDao.getUserFavoritesById(userId = 1L)

        assertEquals(1, retrievedFavorites.size)
        assertEquals(favorite, retrievedFavorites.single())
    }

    /*
     * Verifies that a favorite can be deleted.
     */
    @Test
    fun deleteFavorite_removesFavorite() = runTest {
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

        userDao.insertUser(user)
        favoriteDao.insertFavorite(favorite)

        favoriteDao.deleteFavorite(favorite)

        val retrievedFavorites =
            favoriteDao.getUserFavoritesById(userId = 1L)

        assertTrue(retrievedFavorites.isEmpty())
    }

    /*
     * Verifies the foreign-key cascade rule.
     *
     * When a user is deleted, all favorites belonging to that user should
     * be deleted automatically.
     */
    @Test
    fun deleteUser_deletesUsersFavorites() = runTest {
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

        userDao.insertUser(user)
        favoriteDao.insertFavorite(favorite)

        userDao.deleteUser(user)

        val retrievedFavorites =
            favoriteDao.getUserFavoritesById(userId = 1L)

        assertTrue(retrievedFavorites.isEmpty())
    }
}