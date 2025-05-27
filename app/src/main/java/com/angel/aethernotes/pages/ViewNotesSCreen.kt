package com.angel.aethernotes.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.angel.aethernotes.models.Notes
import com.angel.aethernotes.ui.theme.AetherNotesTheme
import com.angel.aethernotes.viewmodels.NotesViewModel

@Composable
fun ViewNotesScreen(navController:NavHostController) {
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally) {

        var context = LocalContext.current
        var notesRepository = NotesViewModel(navController, context)


        val emptyNotesState = remember { mutableStateOf(Notes("","","","","")) }
        var emptyNotesListState = remember { mutableStateListOf<Notes>() }

        var products = notesRepository.allNotes(emptyNotesState, emptyNotesListState)


        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "All notes",
                fontSize = 30.sp,
                fontFamily = FontFamily.Cursive,
                color = Color.Red)

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(){
                items(products){
                    NotesItem(
                        subject = it.subject,
                        topic = it.topic,
                        subTopic = it.subject,
                        id = it.id,
                        navController = navController,
                        notesRepository = notesRepository,
                        notesFile = it.notesFile
                    )
                }
            }
        }
    }
}


@Composable
fun NotesItem(subject:String, topic:String, subTopic:String, id:String,
                navController:NavHostController,
                notesRepository:NotesViewModel, notesFile:String) {

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = subject)
        Text(text = topic)
        Text(text = subTopic)
        Image(
            painter = rememberAsyncImagePainter(notesFile),
            contentDescription = null,
            modifier = Modifier.size(250.dp)
        )
        Button(onClick = {
            notesRepository.deleteNotes(id)
        }) {
            Text(text = "Delete")
        }
        Button(onClick = {
            //navController.navigate(ROUTE_UPDATE_PRODUCTS+"/$id")
        }) {
            Text(text = "Update")
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ViewProductsScreenPreview(){
    AetherNotesTheme {
        ViewNotesScreen(navController = rememberNavController())
    }
}