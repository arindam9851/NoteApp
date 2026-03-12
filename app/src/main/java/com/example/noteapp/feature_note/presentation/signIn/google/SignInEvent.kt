package com.example.noteapp.feature_note.presentation.signIn.google

sealed class SignInEvent {
    data class GoogleSignIn(val idToken: String): SignInEvent()
    object CheckIfLoggedIn: SignInEvent()
}