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

    fun uploadNotes(subject:String, topic:String, subTopic:String, filePath:Uri){
        val notesId = System.currentTimeMillis().toString()
        val storageRef = FirebaseStorage.getInstance().getReference()
            .child("Notes/$notesId")
        progress.show()
        storageRef.putFile(filePath).addOnCompleteListener{
            progress.dismiss()
            if (it.isSuccessful){
                // Save data to db
                storageRef.downloadUrl.addOnSuccessListener {
                    var notesFileUrl = it.toString()
                    var note = Notes(subject,topic,subTopic,notesFileUrl,notesId)
                    var databaseRef = FirebaseDatabase.getInstance().getReference()
                        .child("Notes/$notesId")
                    databaseRef.setValue(note).addOnCompleteListener {
                        if (it.isSuccessful){
                            Toast.makeText(this.context, "Success", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this.context, "Error", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }else{
                Toast.makeText(this.context, "Upload error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun allNotes(
        note:MutableState<Notes>,
        notes:SnapshotStateList<Notes>):SnapshotStateList<Notes>{
        progress.show()
        var ref = FirebaseDatabase.getInstance().getReference()
            .child("Notes")
        ref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                notes.clear()
                for (snap in snapshot.children){
                    var retrievedNotes = snap.getValue(Notes::class.java)
                    note.value = retrievedNotes!!
                    notes.add(retrievedNotes)
                }
                progress.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "DB locked", Toast.LENGTH_SHORT).show()
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