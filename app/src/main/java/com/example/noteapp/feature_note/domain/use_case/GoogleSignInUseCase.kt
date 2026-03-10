package com.example.noteapp.feature_note.domain.use_case

import com.example.noteapp.feature_note.domain.repository.AuthRepository
import jakarta.inject.Inject

class GoogleSignInUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(idToken: String): Result<Unit> {
        return authRepository.signInWithGoogle(idToken)
    }
}