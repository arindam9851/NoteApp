package com.example.noteapp.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.example.noteapp.feature_note.data.local_data_store.UserPreferences
import com.example.noteapp.feature_note.data.repository.DataStoreRepositoryImpl
import com.example.noteapp.feature_note.domain.repository.DataStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> =
        PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile("user_prefs")
        }

    @Provides
    @Singleton
    fun provideUserPreferences(
        dataStore: DataStore<Preferences>
    ): UserPreferences {
        return UserPreferences(dataStore)
    }


    @Provides
    @Singleton
    fun provideAuthRepository(
        userPreferences: UserPreferences
    ): DataStoreRepository {
        return DataStoreRepositoryImpl(userPreferences)
    }
}