package com.example.noteapp.di

import com.example.noteapp.feature_note.data.repository.FirebaseAuthProvider
import com.example.noteapp.feature_note.domain.repository.AuthProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Provides
    fun provideAuthProvider(): AuthProvider {
        return FirebaseAuthProvider()
    }
}