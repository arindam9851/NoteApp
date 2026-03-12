package com.example.noteapp.feature_note.domain.use_case

import android.util.Log
import com.example.noteapp.feature_note.domain.model.Note
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ShareNoteUserCase {

    operator fun invoke(note: Note): Flow<String?> = callbackFlow {

        try {
            val firestore = FirebaseFirestore.getInstance()
            val noteId = note.id

            val noteData = hashMapOf(
                "title" to note.title,
                "content" to note.content,
                "lastUpdated" to FieldValue.serverTimestamp()
            )

            firestore.collection("notes")
                .document(noteId)
                .set(noteData)
                .addOnSuccessListener {

                    val url = "https://noteapp-1187e.web.app/note/$noteId"
                    trySend(url)
                    close()
                    Log.d("ShareNote", "Note shared URL: $url")
                }
                .addOnFailureListener { e ->
                    trySend(null)
                    close()
                    Log.e("ShareNote", "Error sharing note", e)
                }

        } catch (e: Exception) {
            trySend(null)
            close(e)
            Log.e("ShareNote", "Unexpected error", e)
        }

        awaitClose { }
    }
}

