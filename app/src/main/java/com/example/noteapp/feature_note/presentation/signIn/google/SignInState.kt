package com.example.noteapp.feature_note.presentation.signIn.google

data class SignInState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val isPhoneLinked: Boolean = false,
    val hasNavigated: Boolean = false,
    val navigationTarget: String? = null
)
