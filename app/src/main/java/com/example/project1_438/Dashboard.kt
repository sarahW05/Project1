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
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DashboardScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Row(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            //button for user page
            IconButton(onClick = {//input logic
             }) {
                //person icon
                Text("\uD83D\uDC64")
            }

            //log in button
            Button(
                onClick = {
                    //logic
                }) {
                Text("Log In")
            }
        }

        Spacer(
            modifier = Modifier.height(160.dp)
        )

        //word from api??
        Text(
            text = "Random Word"
        )

        Spacer(
            modifier = Modifier.height(200.dp)
        )

        //search bar
        OutlinedTextField(
            value = "",
            onValueChange = {
                //input logic
            },
            label = {
                Text("Search up a word")
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}