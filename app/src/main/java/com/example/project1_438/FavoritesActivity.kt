package com.example.project1_438

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.project1_438.ui.theme.Project1438Theme
import java.util.ArrayList;

class FavoritesActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        ArrayList<String> list = new ArrayList<>();
        list.add("Test");
        list.add("Favorite");
        list.add("Words");
        list.add("List");
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Project1438Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val pagerState = rememberPagerState(pageCount = {
                        list.size();
                    })
                    HorizontalPager(state = pagerState) { page ->
                        Text(
                            text = "Page: $page",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}