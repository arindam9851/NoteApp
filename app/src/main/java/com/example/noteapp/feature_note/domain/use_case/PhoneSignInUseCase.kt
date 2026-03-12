package com.example.noteapp.feature_note.domain.use_case

import android.app.Activity
import com.example.noteapp.feature_note.domain.repository.AuthRepository

class PhoneSignInUseCase(
    private val authRepository: AuthRepository

) {
    /**
     * Sends OTP to the given phone number.
     * Returns Result containing verificationId on success.
     */
    suspend fun invokeSendOtp(phone: String, activity: Activity): Result<String> {
        return authRepository.sendOtp(phone, activity)
    }

    /**
     * Verifies OTP and links phone to current user.
     * Returns Result<Unit> indicating success or failure.
     */
    suspend fun invokeVerifyOtp(verificationId: String, otp: String): Result<Unit> {
        return authRepository.verifyOtp(verificationId, otp)
    }

    suspend fun invokeVerifyOtpForNewUser(verificationId: String, otp: String): Result<Unit> {
        return authRepository.verifyOtpForNewUser(verificationId, otp)
    }
}