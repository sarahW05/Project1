package com.example.project1_438.database

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.ForeignKey

//Apparently to relate between two tables, we need args in the child entity tag
@Entity(
//    Got a little help from Mr. Gpt with code examples on how to set this up
//    This is how to create a composite key
    primaryKeys = ["userId", "word"],
//    The references to the other object and how they relate
    foreignKeys = [
//        The actual declaration for generating the foreign key
        ForeignKey(
//            Tells the declaration the parent entity
            entity = User::class,
//            referenced column in parent
            parentColumns = ["userId"],
//            The F-Key column in here
            childColumns = ["userId"],
//            Deletes all favorites if user is deleted
            onDelete = ForeignKey.CASCADE
        )

    ]
)
data class Favorite(
//    Declaring a primary key here is redundant, because the args up in entity do it for us
//    @PrimaryKey val favListId: Int,
//    The foreign key, we don't want to mutate, so store as val
    @ColumnInfo(name = "userId") val userId: Long,
    @ColumnInfo(name = "word") val word: String
    )
