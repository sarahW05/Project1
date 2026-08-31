package com.example.project1_438

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query

// This is the file where we put the Query Functions for the database to add, retrieve, or delete data
@Dao
interface UserDAO {
//    The query to just grab all co,uns from the User table, if needed
    @Query("SELECT * FROM User")
//    The suspend keyword lets teh function be paused and resumed later once it gets a response
    suspend fun getAllUsers(): List<User>

//    Allows us to grab all users and list them by ID value
    @Query("SELECT * FROM User Where UserId IN (:userIds)")
    suspend fun loadAllByIds(userIds: IntArray): List<User>

//    Grab a single user by their ID
    @Query("SELECT * FROM User WHERE UserId LIKE UserId")
    suspend fun getUserById(): User

//    Our Insertion Function
    @Insert
    suspend fun insertAll(vararg users: User)
//    It takes a vararg, which must be a User object, and adds all of the values/variables to a new row in the table

//    Delete Function (most databases don't actually use this, they just tag a value as deleted to retrieve if needed)
    @Delete
    suspend fun delete(user: User)
}