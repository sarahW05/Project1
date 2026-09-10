package com.example.project1_438

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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

                NavHost(
                    navController = navController,
                    startDestination = "dashboard"
                ){
                    composable("dashboard"){
                        DashboardScreen(onLoginClick = {
                            navController.navigate("login")
                        })
                    }
                    composable("login"){
                        //navigates to dashboard once user is successfully logged in
                        LoginScreen(userDao = database.userDAO(), onLoginSuccess = {navController.navigate("dashboard")})
                    }
                }

            }
        }
    }
}