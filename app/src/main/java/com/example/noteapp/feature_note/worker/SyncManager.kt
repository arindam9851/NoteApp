package com.example.noteapp.feature_note.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import java.util.concurrent.TimeUnit

class SyncManager @Inject constructor(
    @param:ApplicationContext private val context: Context
)  {

    fun scheduleDailySync() {
        val request =
            PeriodicWorkRequestBuilder<NoteSyncWorker>(
                1, TimeUnit.DAYS
            )
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .build()
        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                "note_sync",
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
    }
}