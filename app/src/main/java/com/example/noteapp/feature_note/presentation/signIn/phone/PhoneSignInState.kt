package com.example.noteapp.feature_note.presentation.signIn.phone

data class PhoneSignInState(
    val phone: String = "",
    val otp: String = "",
    val verificationId: String? = null,
    val isLoading: Boolean = false,
    val isVerified: Boolean = false,
    val error: String? = null
)
