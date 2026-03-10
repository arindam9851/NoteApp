package com.example.noteapp.feature_note.presentation.signIn

sealed class SignInEvent {
    data class GoogleSignIn(val idToken: String): SignInEvent()
}