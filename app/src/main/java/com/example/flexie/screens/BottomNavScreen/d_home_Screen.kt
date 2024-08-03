package com.example.flexie.screens.BottomNavScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flexie.ui.theme.darkBlue

@Composable
fun d_home_Screen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBlue)
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(20) {
                Text(
                    text = "Item #$it",
                    color = Color.White,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            }
        }
        CustomFloatingActionButton()
    }
}

@Preview(showSystemUi = true)
@Composable
fun CustomFloatingActionButton() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        homeFloatingActionButton()
    }
}

@Preview(showSystemUi = true)
@Composable
fun homeFloatingActionButton() {
   Box(
        modifier = Modifier
            .padding(bottom = 70.dp) // Adjust the padding to place the FAB properly
            .height(45.dp)
            .fillMaxWidth(.5f)
            .clip(shape = RoundedCornerShape(25.dp))
            .background(Color.DarkGray)
       , // Adjust the height to make the FAB visible
       contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxHeight().padding(start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val list = listOf("Movies", "Series", "Anime", "Shows")
            list.forEach {
                Text(text = it, color = Color.White, modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = 13.sp)
                if(it != "Shows") {
                    Spacer(
                        modifier = Modifier.fillMaxHeight(.45f).width(.5.dp)
                            .background(Color.LightGray)
                    )
                }
            }
        }
    }
}