package com.example.noteapp.feature_note.domain.repository

interface AuthRepository {
    suspend fun signInWithGoogle(idToken: String):Result<Unit>
}