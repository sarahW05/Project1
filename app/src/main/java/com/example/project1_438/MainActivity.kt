package com.example.project1_438

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.project1_438.ui.theme.Project1438Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //get db before setting content
        val database = (application as ProjectApplication).database
        setContent {
            Project1438Theme {
                //the thing that controls the ability to move between the screens/views
                val navController = rememberNavController()
                var loggedInUserId by remember { mutableStateOf<Long?>(null) }

                NavHost(
                    navController = navController,
                    startDestination = "dashboard"
                ){
                    composable("dashboard") {
                        DashboardScreen(
                            onLoginClick = {
                                navController.navigate("login")
                            },
                            onUserClick = {
                                if (loggedInUserId != null) {
                                    navController.navigate("user")
                                } else {
                                    navController.navigate("login")
                                }
                            },
                            onSearch = { word ->
                                val intent =
                                    Intent(
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
                    composable("login"){
                        //navigates to dashboard once user is successfully logged in
                        LoginScreen(userDao = database.userDAO(), onLoginSuccess = {userId -> loggedInUserId = userId
                            navController.navigate("dashboard")})
                    }
                    composable("user") {
                        loggedInUserId?.let { userId -> UserPage(userId = userId, userDao = database.userDAO())}
                    }
                }

            }
        }
    }
}