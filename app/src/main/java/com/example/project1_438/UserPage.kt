package com.example.project1_438

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.project1_438.database.User
import com.example.project1_438.database.UserDao
import kotlinx.coroutines.launch

@Composable
fun UserPage(userId: Long, userDao: UserDao){

    var user by remember { mutableStateOf<User?>(null) }

    //make text field editable
    var isEditing by remember { mutableStateOf(false) }

    //editable fields
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    //gets user info from db
    LaunchedEffect(userId) {
        val currentUser = userDao.getUserById(userId)

        if(currentUser != null){
            user = currentUser
            //db info into editable fields
            firstName = currentUser.firstName
            lastName = currentUser.lastName
            username = currentUser.userName
            password = currentUser.password
        }
    }
    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        //not editing
    if(!isEditing) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column {
                Text(
                    text = "Name: ${user?.firstName ?: ""} ${user?.lastName ?: ""}"
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Text(
                    text = "Username: ${user?.userName ?: ""}"
                )
            }
            Button(
                onClick = {
                    isEditing = true
                }
            ) {
                Text("Edit Information")
            }
        }
        Spacer(
            modifier = Modifier.height(140.dp)
        )

        //button to favorite words page
        Button(
            onClick = {
                //navigation to fav words page
            }
        ) {
            Text("Favorite Words")
        }
    }else{
        Text("Edit Information")
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(value = firstName, onValueChange = {firstName = it},
            label = {Text("First Name")}, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(value = lastName, onValueChange = {lastName = it},
            label = {Text("Last Name")}, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(value = username, onValueChange = {username = it},
            label = {Text("Username")}, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(value = password, onValueChange = {password = it},
            label = {Text("Password")}, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(15.dp))

        //save info button
        Button(onClick = {
            val currentUser = user
            if(currentUser != null){
                //update User obj
                currentUser.firstName = firstName
                currentUser.lastName = lastName
                currentUser.userName = username
                currentUser.password = password

                scope.launch {
                    userDao.updateUser(currentUser)
                    user = currentUser
                    isEditing = false
                }
            }
        },
            modifier = Modifier.fillMaxWidth()
        ){
            Text("Save Changes")
        }

        Spacer(modifier = Modifier.height(10.dp))

        //cancel
        Button(onClick = {
            user?.let { currentUser ->
                firstName = currentUser.firstName
                lastName = currentUser.lastName
                username = currentUser.userName
                password = currentUser.password
            }
            isEditing = false
        },
            modifier = Modifier.fillMaxWidth()){
            Text("Cancel")
        }
    }
    }
}

