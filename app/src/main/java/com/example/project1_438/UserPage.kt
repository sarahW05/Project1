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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun UserPage(){
    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ){
            Column{
                Text(
                    text="Name: Name of User"
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Text(
                    text="Username: username"
                )
            }
            Button(
                onClick = {
                    //logic
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
    }
}

