package com.example.project1_438.database

import androidx.room3.AutoMigration
import androidx.room3.Database
import androidx.room3.RoomDatabase

//Added a simple auto migration so that we can have database versions just move forward
@Database(
    entities = [User::class, Favorite::class],
    version = 2,
//    autoMigrations =
//        [AutoMigration(
//        from = 1,
//        to = 2)]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDAO() : UserDao
    abstract fun favoritesDAO() : FavoriteDao
}