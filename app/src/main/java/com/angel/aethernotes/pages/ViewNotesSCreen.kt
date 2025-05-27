package com.angel.aethernotes.pages

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
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
fun ViewNotesScreen(navController: NavHostController) {
    val context = LocalContext.current
    val notesRepository = NotesViewModel(navController, context)

    val emptyNotesState = remember { mutableStateOf(Notes("", "", "", "", "")) }
    val emptyNotesListState = remember { mutableStateListOf<Notes>() }

    val notesList = notesRepository.allNotes(emptyNotesState, emptyNotesListState)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "All Notes",
            fontSize = 30.sp,
            fontFamily = FontFamily.Cursive,
            color = Color.Red
        )

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {
            items(notesList) { note ->
                NotesItem(
                    subject = note.subject,
                    topic = note.topic,
                    subTopic = note.subTopic,
                    id = note.id,
                    navController = navController,
                    notesRepository = notesRepository,
                    notesFile = note.notesFile,
                    context = context
                )
                Divider(thickness = 1.dp, color = Color.Gray, modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}


@Composable
fun NotesItem(
    subject: String,
    topic: String,
    subTopic: String,
    id: String,
    navController: NavHostController,
    notesRepository: NotesViewModel,
    notesFile: String,
    context: Context
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Text(text = "Subject: $subject", fontWeight = FontWeight.Bold)
        Text(text = "Topic: $topic")
        Text(text = "Sub-topic: $subTopic")

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = {
                openPdf(context, notesFile)
            }) {
                Text(text = "View PDF")
            }

            Button(onClick = {
                notesRepository.deleteNotes(id)
            }) {
                Text(text = "Delete")
            }

            Button(onClick = {
                // Uncomment and update your navigation logic here
                // navController.navigate("update_note_screen/$id")
            }) {
                Text(text = "Update")
            }
        }
    }
}

fun openPdf(context: Context, pdfUrl: String) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(Uri.parse(pdfUrl), "application/pdf")
        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_GRANT_READ_URI_PERMISSION
    }

    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "No PDF viewer found", Toast.LENGTH_SHORT).show()
    }
}



@Composable
@Preview(showBackground = true)
fun ViewProductsScreenPreview(){
    AetherNotesTheme {
        ViewNotesScreen(navController = rememberNavController())
    }
}