package com.example.project1_438

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import com.example.project1_438.ui.theme.Project1438Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Project1438Theme {
                //will change to homepage after creating the view
                LoginScreen()
            }
        }
    }
}