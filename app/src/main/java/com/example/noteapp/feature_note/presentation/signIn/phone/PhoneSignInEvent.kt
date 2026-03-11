package com.example.noteapp.feature_note.presentation.signIn.phone

import android.app.Activity

sealed class PhoneSignInEvent {
    data class EnterPhone(val phone: String) : PhoneSignInEvent()
    data class EnterOtp(val otp: String) : PhoneSignInEvent()
    data class SendOtp(val activity: Activity) : PhoneSignInEvent()
    object VerifyOtp : PhoneSignInEvent()
}