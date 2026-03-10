package com.example.noteapp.feature_note.data.repository

import com.example.noteapp.feature_note.data.local_data_store.UserPreferences
import com.example.noteapp.feature_note.domain.repository.DataStoreRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class DataStoreRepositoryImpl @Inject constructor(
    private val userPreferences: UserPreferences
) : DataStoreRepository {
    override suspend fun saveLoginState(isLoggedIn: Boolean) {
        userPreferences.saveLoginState(isLoggedIn)
    }

    override fun getLoginState(): Flow<Boolean> {
        return userPreferences.isLoggedIn
    }

}