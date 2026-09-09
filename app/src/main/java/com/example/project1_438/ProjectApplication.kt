package com.example.project1_438

import android.app.Application
import androidx.room3.Room
import androidx.sqlite.driver.AndroidSQLiteDriver
import com.example.project1_438.database.AppDatabase
import com.example.project1_438.database.UserDao
import com.example.project1_438.database.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

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

//    This is to create a couple test users when the application runs, if they don't exist
    override fun onCreate() {
//        This has the normal startup code run before the rest of what we're doing in here
        super.onCreate()

//    This makes it so the test user insertion happens in another thread, because we have to to call a Suspend Function
    CoroutineScope(
        SupervisorJob() + Dispatchers.IO).launch {
//            Add test users to the database, if they don't already exist
            addTestUsers(database.userDAO())
        }
    }
}

//  The actual method definition for creating the test users and inserting them into the database
private suspend fun addTestUsers(userDao: UserDao){
//    Creates a list of user elements to be inserted
    val testUsers = listOf(User(userName = "testUser1", firstName = "User", lastName = "Test1", password = "password1"),
        User(userName = "testUser2", firstName = "User", lastName = "Test2", password = "password2"))

//    The check for if they exist in the database
    for(user in testUsers){
        if(userDao.getUserByUserName(user.userName) == null){
            userDao.insertUser(user)
        }
    }

}