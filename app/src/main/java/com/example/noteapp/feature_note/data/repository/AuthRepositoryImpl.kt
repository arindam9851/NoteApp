package com.example.noteapp.feature_note.data.repository

import android.app.Activity
import com.example.noteapp.feature_note.domain.repository.AuthRepository
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import jakarta.inject.Inject
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : AuthRepository {
    override suspend fun signInWithGoogle(idToken: String): Result<Unit> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            firebaseAuth.signInWithCredential(credential).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)

        }
    }

    override suspend fun sendOtp(phone: String, activity: Activity): Result<String> =
        suspendCancellableCoroutine { cont ->
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // Auto verify - optional
            }
            override fun onVerificationFailed(e: FirebaseException) {
                if (cont.isActive) cont.resume(Result.failure(e)) { _, _, _ -> }
            }
            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                if (cont.isActive) cont.resume(Result.success(verificationId)) { _, _, _ -> }
            }
        }

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override suspend fun verifyOtp(verificationId: String, otp: String): Result<Unit> = try {
        val credential = PhoneAuthProvider.getCredential(verificationId, otp)
        val user = firebaseAuth.currentUser
            ?: return Result.failure(Exception("No logged-in user to link"))

        user.linkWithCredential(credential).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun verifyOtpForNewUser(verificationId: String, otp: String): Result<Unit> = try {
        val credential = PhoneAuthProvider.getCredential(verificationId, otp)
        // Sign in directly (treat as new user)
        firebaseAuth.signInWithCredential(credential).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }


}