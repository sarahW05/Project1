package com.example.project1_438

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.project1_438.ui.theme.Project1438Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Gets the database before setting the Compose content.
        val database = (application as ProjectApplication).database

        // Gets the session manager before setting the Compose content.
        // The session manager provides access to the persisted logged-in user ID.
        val sessionManager = (application as ProjectApplication).sessionManager

        setContent {
            Project1438Theme {
                // Controls the ability to move between the application's screens.
                val navController = rememberNavController()

                // Safely reads the logged-in user's ID from DataStore.
                // The value is null when no user is currently logged in.
                val loggedInUser by sessionManager.userIdFlow()
                    .collectAsStateWithLifecycle(initialValue = null)

                NavHost(
                    navController = navController,
                    startDestination = "dashboard"
                ) {
                    composable("dashboard") {
                        DashboardScreen(
                            loggedInUserId = loggedInUser,
                            onLoginClick = {
                                navController.navigate("login")
                            },
                            onUserClick = {
                                // Logged-in users can open their user page.
                                // Logged-out users are sent to the login screen.
                                if (loggedInUser != null) {
                                    navController.navigate("user")
                                } else {
                                    navController.navigate("login")
                                }
                            },
                            onSearch = { word ->
                                // Opens the definition activity with the searched word.
                                val intent = Intent(
                                    this@MainActivity,
                                    DefinitionActivity::class.java
                                ).putExtra(
                                    DefinitionActivity.EXTRA_WORD,
                                    word
                                )

                                startActivity(intent)
                            }
                        )
                    }

                    composable("login") {
                        // Navigates to the dashboard after a successful login.
                        // LoginScreen also saves the user's ID through SessionManager.
                        LoginScreen(
                            userDao = database.userDAO(),
                            sessionManager = sessionManager,
                            onLoginSuccess = {
                                navController.navigate("dashboard")
                            },
                            onCreateAccount = {
                                // Allows users to reach account creation from
                                // the login screen.
                                navController.navigate("createAcc")
                            }
                        )
                    }

                    composable("createAcc") {
                        // Creates a new account and saves the new user's ID
                        // through SessionManager before returning to the dashboard.
                        CreateAccount(
                            userDao = database.userDAO(),
                            sessionManager = sessionManager,
                            onAccCreated = {
                                navController.navigate("dashboard")
                            }
                        )
                    }

                    composable("user") {
                        // Only displays the user page when a logged-in user ID
                        // is available from the session manager.
                        loggedInUser?.let { userId ->
                            UserPage(
                                userId = userId,
                                userDao = database.userDAO()
                            )
                        }
                    }
                }
            }
        }
    }
}