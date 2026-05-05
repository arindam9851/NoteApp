package com.example.noteapp.feature_note.data.repository

import com.example.noteapp.feature_note.domain.repository.AuthProvider
import com.google.firebase.auth.FirebaseAuth

class FirebaseAuthProvider : AuthProvider {
    override fun getUserId(): String {
        return FirebaseAuth.getInstance().currentUser?.uid ?: ""
    }
}