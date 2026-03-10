package com.example.noteapp.feature_note.presentation.signIn

data class SignInState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)
