package com.example.project1_438

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project1_438.database.SessionManager
import com.example.project1_438.database.User
import com.example.project1_438.database.UserDao
import kotlinx.coroutines.launch

//A lot of this code is almost identical to the login screen, so I'm copy-pasting chunks as a start
@Composable
fun CreateAccount(userDao: UserDao, sessionManager: SessionManager, onAccCreated: () -> Unit){
    //variables created to store user information. They start as empty but get updated as user types
    var username by remember { mutableStateOf("") }
    var fname by remember { mutableStateOf("") }
    var lname by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }

    //error message for if username/password isn't in db
    var errorMessage by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().padding(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ){

        Text(
            text = "Create An Account",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
        //this adds textbox

        OutlinedTextField(
            value = username,
            onValueChange = {username = it},
            label = {Text("Username")},
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(15.dp)
        )

        OutlinedTextField(
            value = fname,
            onValueChange = {fname = it},
            label = {Text("First Name")},
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(15.dp)
        )

        OutlinedTextField(
            value = lname,
            onValueChange = {lname = it},
            label = {Text("Last Name")},
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(15.dp)
        )
//        Create a fillable text box
        OutlinedTextField(
            value = password,
            onValueChange = {password = it},
            label = {Text("Password")},
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(25.dp)
        )

        OutlinedTextField(
            value = confirm,
            onValueChange = {confirm = it},
            label = {Text("Confirm Password")},
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(25.dp)
        )
//    Creating a button and have it call logic on press
        Button(
            onClick = {
                scope.launch {
                    // Check that all fields are filled out
                    if(username.isBlank() ||
                        fname.isBlank() ||
                        lname.isBlank()||
                        password.isBlank()||
                        confirm.isBlank()){
                        errorMessage = "All Fields Required for Account Creation."

                    }
                    else if(password != confirm){ // Checks if entered password values match
                        errorMessage = "Passwords must match"
                    } // Check if the user already exists
                    else if (userDao.getUserByUserName(userName = username) != null){
                        errorMessage = "Username already in use"
                    }
                    else{
                        // inserts the user if they both don't exist and the passwords match
                        val uid = userDao.insertUser(
                            User(
                                userName = username,
                                firstName = fname,
                                lastName = lname,
                                password = password
                            )
                        )

                        if (uid != null){
                            errorMessage = ""
                            //Saves teh logged in users info
                            sessionManager.saveUserId(uid)
                            onAccCreated()
                        }
                        else{
                            errorMessage = "Database experienced an error"
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()){
            Text("Create Account")
        }
        //if there is an error message display it
        if(errorMessage.isNotEmpty()){
            Spacer(modifier = Modifier.height(10.dp))
            Text(errorMessage)
        }

    }


}