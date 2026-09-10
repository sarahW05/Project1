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
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project1_438.database.UserDao

@Composable
fun LoginScreen(userDao: UserDao, onLoginSuccess: () -> Unit) {
    //variables created to store user information. They start as empty but get updated as user types
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    //error message for if username/password isn't in db
    var errorMessage by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().padding(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "YourDictionary",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Text(
            text = "Log In",
            fontSize = 20.sp
        )

        Spacer(
            modifier = Modifier.height(30.dp)
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
            value = password,
            onValueChange = {password = it},
            label = {Text("Password")},
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(25.dp)
        )

        Button(onClick = {
            scope.launch {
                val user = userDao.login(username, password)
                if(user != null){
                    errorMessage = ""
                    onLoginSuccess()
                }
                else{
                    errorMessage = "Incorrect username or password"
                }
            }
        },
            modifier = Modifier.fillMaxWidth()) {
            Text("Log In")
        }
        //if there is an error message display it
        if(errorMessage.isNotEmpty()){
            Spacer(modifier = Modifier.height(10.dp))
            Text(errorMessage)
        }
    }
}