package com.example.project1_438

import android.app.Application
import androidx.room3.Room
import androidx.sqlite.driver.AndroidSQLiteDriver
import com.example.project1_438.database.AppDatabase

// Had to use Mr. Gpt to figure out how to do this, god Kotlin documentation sucks
class ProjectApplication : Application() {
//    Creates one database instance per application process using it, stores it in memory to re-use for each,
//    Because of the by lazy structural instruction (Android documentation sucks)
    val database: AppDatabase by lazy {
        Room.databaseBuilder<AppDatabase>(
            applicationContext,
            "project1_database" //The persistent database filename
        ).setDriver(AndroidSQLiteDriver()).build()
    }
}