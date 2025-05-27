package com.angel.aethernotes.pages

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.angel.aethernotes.ui.theme.AetherNotesTheme
import com.angel.aethernotes.viewmodels.NotesViewModel

@Composable
fun AddNotesScreen(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Add Notes",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Cursive
        )

        var notesSubject by remember { mutableStateOf("") }
        var topic by remember { mutableStateOf("") }
        var subTopic by remember { mutableStateOf("") }
        val context = LocalContext.current

        Spacer(modifier = Modifier.height(30.dp))

        OutlinedTextField(
            value = notesSubject,
            onValueChange = { notesSubject = it },
            label = { Text(text = "Subject *") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = topic,
            onValueChange = { topic = it },
            label = { Text(text = "Topic *") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = subTopic,
            onValueChange = { subTopic = it },
            label = { Text(text = "Sub topic *") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        Spacer(modifier = Modifier.height(20.dp))

        //---------------------PDF PICKER START-----------------------------------//
        val modifier = Modifier
        FilePicker(
            modifier = modifier,
            context = context,
            navController = navController,
            subject = notesSubject.trim(),
            topic = topic.trim(),
            subTopic = subTopic.trim()
        )
        //---------------------PDF PICKER END-----------------------------------//
    }
}

@Composable
fun FilePicker(
    modifier: Modifier = Modifier,
    context: Context,
    navController: NavHostController,
    subject: String,
    topic: String,
    subTopic: String
) {
    var hasFile by remember { mutableStateOf(false) }
    var notesFileUri by remember { mutableStateOf<Uri?>(null) }

    val filePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            hasFile = uri != null
            notesFileUri = uri
        }
    )

    Column(modifier = modifier) {
        if (hasFile && notesFileUri != null) {
            val fileName = notesFileUri?.lastPathSegment ?: "Selected PDF"
            Text(
                text = "Selected: $fileName",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(8.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    filePicker.launch("application/pdf")
                },
            ) {
                Text(text = "Select PDF Notes")
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (notesFileUri != null) {
                        val productRepository = NotesViewModel(navController, context)
                        productRepository.uploadNotes(subject, topic, subTopic, notesFileUri!!)
                    } else {
                        Toast.makeText(context, "Please select a file first", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text(text = "Upload")
            }
        }
    }
}
