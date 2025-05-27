package com.angel.aethernotes.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.angel.aethernotes.navigation.ADD_NOTES_URL
import com.angel.aethernotes.navigation.VIEW_NOTES_URL
import com.angel.aethernotes.ui.theme.AetherNotesTheme

@Composable
fun HomeScreen(navController:NavHostController){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Aether Notes",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Cursive
        )
        Spacer(modifier = Modifier.height(30.dp))
        Button(onClick = {
            navController.navigate(ADD_NOTES_URL)
        }) {
            Text(text = "Add Notes")
        }
        Spacer(modifier = Modifier.height(30.dp))
        Button(onClick = {
            navController.navigate(VIEW_NOTES_URL)
        }) {
            Text(text = "View Notes")
        }
    }
}

@Composable
@Preview(showBackground = true)
fun HomeScreenPreview(){
    AetherNotesTheme {
        HomeScreen(navController = rememberNavController())
    }
}