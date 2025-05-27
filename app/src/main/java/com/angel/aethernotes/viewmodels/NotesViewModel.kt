package com.angel.aethernotes.viewmodels

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation.NavHostController
import com.angel.aethernotes.models.Notes
import com.angel.aethernotes.navigation.LOGIN_URL
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class NotesViewModel(var navController:NavHostController, var context: Context) {
    var authViewModel:AuthViewModel
    var progress:ProgressDialog
    init {
        authViewModel = AuthViewModel(navController, context)
        if (!authViewModel.isLoggedIn()){
            navController.navigate(LOGIN_URL)
        }
        progress = ProgressDialog(context)
        progress.setTitle("Loading")
        progress.setMessage("Please wait...")
    }

    fun uploadNotes(subject: String, topic: String, subTopic: String, filePath: Uri) {
        val notesId = System.currentTimeMillis().toString()
        val storageRef = FirebaseStorage.getInstance().getReference("Notes/$notesId.pdf")

        val contentResolver = context.contentResolver
        val mimeType = contentResolver.getType(filePath)

        if (mimeType != "application/pdf") {
            Toast.makeText(context, "Only PDF files are supported", Toast.LENGTH_SHORT).show()
            return
        }

        progress.show()

        storageRef.putFile(filePath).addOnCompleteListener {
            progress.dismiss()
            if (it.isSuccessful) {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    val notesFileUrl = uri.toString()
                    val note = Notes(subject, topic, subTopic, notesFileUrl, notesId)

                    val databaseRef = FirebaseDatabase.getInstance().getReference("Notes/$notesId")
                    databaseRef.setValue(note).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "Note uploaded successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Failed to save note info", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(context, "PDF upload failed", Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun allNotes(
        note: MutableState<Notes>,
        notes: SnapshotStateList<Notes>
    ): SnapshotStateList<Notes> {
        progress.show()

        val ref = FirebaseDatabase.getInstance().getReference("Notes")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                notes.clear()
                for (snap in snapshot.children) {
                    val retrievedNotes = snap.getValue(Notes::class.java)
                    if (retrievedNotes != null) {
                        note.value = retrievedNotes
                        notes.add(retrievedNotes)
                    }
                }
                progress.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {
                progress.dismiss()
                Toast.makeText(context, "Failed to retrieve notes", Toast.LENGTH_SHORT).show()
            }
        })

        return notes
    }


    fun deleteNotes(notesId:String){
        var ref = FirebaseDatabase.getInstance().getReference()
            .child("Notes/$notesId")
        ref.removeValue()
        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
    }
}