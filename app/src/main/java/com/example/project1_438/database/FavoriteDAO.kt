package com.example.project1_438.database

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.OnConflictStrategy

//This is gonna be fairly simple, we only need it to fetch, insert, or delete
@Dao
interface FavoriteDao {
//    Grabbing th whole table for testing purposes
    @Query("SELECT * FROM Favorite")
    suspend fun getAllFavorites(): List<Favorite>

//    Grabbing more specifically the favs of one user
    @Query("SELECT * FROM Favorite WHERE userId = :userId")
    suspend fun getUserFavoritesById(userId: Long): List<Favorite>

//    Adding a favorite to a users favorites
    @Insert(onConflict = OnConflictStrategy.IGNORE) // This will ignore duplicate entry attempts for already favorited words
    suspend fun insertFavorite(favorite: Favorite)

//    Adding multiple favorites at once
    @Insert(onConflict = OnConflictStrategy.IGNORE) // This will ignore duplicate entry attempts for already favorited words
    suspend fun insertFavorites(favorites: List<Favorite>)

//    Removing favorites
    @Delete
    suspend fun deleteFavorite(favorite: Favorite)

}