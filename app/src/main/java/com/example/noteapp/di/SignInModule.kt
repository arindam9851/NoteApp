package com.example.noteapp.di

import com.example.noteapp.feature_note.data.repository.AuthRepositoryImpl
import com.example.noteapp.feature_note.domain.repository.AuthRepository
import com.example.noteapp.feature_note.domain.use_case.GoogleSignInUseCase
import com.example.noteapp.feature_note.domain.use_case.PhoneSignInUseCase
import com.example.noteapp.feature_note.domain.use_case.SignInUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SignInModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(auth: FirebaseAuth): AuthRepository{
       return AuthRepositoryImpl(auth)
    }

    @Provides
    @Singleton
    fun provideSignInUseCase(authRepository: AuthRepository): SignInUseCase {
        return SignInUseCase(
            googleSignInUseCase = GoogleSignInUseCase(authRepository),
            phoneSignInUseCase = PhoneSignInUseCase(authRepository)
        )
    }
}
