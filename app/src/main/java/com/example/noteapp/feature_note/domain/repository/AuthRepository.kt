package com.example.noteapp.feature_note.domain.repository

import android.app.Activity

interface AuthRepository {
    suspend fun signInWithGoogle(idToken: String):Result<Unit>

    suspend fun sendOtp(phone: String, activity: Activity): Result<String> // returns verificationId
    suspend fun verifyOtp(verificationId: String, otp: String): Result<Unit>

    suspend fun verifyOtpForNewUser(verificationId: String, otp: String): Result<Unit>

}