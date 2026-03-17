package com.example.noteapp.feature_note.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.noteapp.feature_note.domain.repository.NoteRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.tasks.await

@HiltWorker
class NoteSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repository: NoteRepository
): CoroutineWorker(context, params){

    override suspend fun doWork(): Result {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
            ?: return Result.success()

        val firestore = FirebaseFirestore.getInstance()
        val notes = repository.getUnsyncedNotes()
        try {
            notes.forEach { note ->
                val data = mapOf(
                    "title" to note.title,
                    "content" to note.content,
                    "color" to note.color,
                    "updatedAt" to note.updatedAt
                )
                firestore.collection("users")
                    .document(userId)
                    .collection("notes")
                    .document(note.id)
                    .set(data)
                    .await()
                repository.markSynced(note.id)
            }
            return Result.success()

        } catch (e: Exception) {
            Log.e("NoteSyncWorker", "Error syncing notes: ${e.message}")
            return Result.retry()

        }
    }


}