package com.example.project1_438.database

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

//Documentation used:
// DataStore: https://developer.android.com/topic/libraries/architecture/datastore#kts
// Datastore preferences key types: https://developer.android.com/reference/kotlin/androidx/datastore/preferences/core/package-summary#longPreferencesKey(kotlin.String)
// Apparently we need this to be in a class: https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:datastore/datastore-sampleapp/src/main/java/com/example/datastore/snippets/preferences/PreferencesDataStore.kt

//Creates a local storage file to hold onto information
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

//Wrapper class
class SessionManager(private val context: Context){
    //Identifies the saved users login key (userId)
    val loggedInUser = longPreferencesKey("logged_in_user")

    //the function that defines the key
    fun userIdFlow(): Flow<Long?> =
        context.dataStore.data.map { preferences -> preferences[loggedInUser]  }

    // Suspended functions for saving the users ID and clearing the stored ID
    suspend fun saveUserId(userId: Long){
        // Takes a userId passed from the context, and sets it to be stored with the key
        context.dataStore.edit { preferences -> preferences[loggedInUser] = userId }
    }
    // Just removes the stored value
    suspend fun clearUserId(){
        context.dataStore.edit { preferences -> preferences.remove(loggedInUser)}
    }


}
