package com.example.noteapp.feature_note.domain.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun saveLoginState(isLoggedIn: Boolean)
    fun getLoginState(): Flow<Boolean>
}