package com.example.project1_438

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
        //get db before setting content
        val database = (application as ProjectApplication).database
        // get the session manager before setting content
        val sessionManager = (application as ProjectApplication).sessionManager
        setContent {
            Project1438Theme {
                //the thing that controls the ability to move between the screens/views
                val navController = rememberNavController()

                // this allows us to safely read from the DataStore without directly accessing it
                // got Mr. Gippity to help by explaining a couple of things
                val loggedInUser by sessionManager.userIdFlow().collectAsStateWithLifecycle(initialValue = null)

                NavHost(
                    navController = navController,
                    startDestination = "dashboard"
                ){
                    composable("dashboard"){
                        DashboardScreen(
                            loggedInUserId = loggedInUser,
                            onLoginClick = {
                            navController.navigate("login")
                        })
                    }
                    composable("login"){
                        //navigates to dashboard once user is successfully logged in
                        LoginScreen( //Expanded this a little because it was getting long
                            userDao = database.userDAO(),
                            sessionManager = sessionManager,
                            onLoginSuccess = {navController.navigate("dashboard")},
                            onCreateAccount = {navController.navigate("createAcc")})
                    }
                    composable("createAcc"){
                        CreateAccount(
                            userDao = database.userDAO(),
                            sessionManager = sessionManager,
                            onAccCreated = {navController.navigate("dashboard")}
                        )
                    }
                }

            }
        }
    }
}