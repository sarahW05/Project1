package com.example.project1_438.database

import androidx.room3.Database
import androidx.room3.RoomDatabase

@Database(entities = [User::class, Favorite::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDAO() : UserDao
    abstract fun favoritesDAO() : FavoriteDao
}