package com.example.noteapp.feature_note.presentation.utils.snackbar

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object SnackBarManager {
    private val _snackbarFlow = MutableSharedFlow<String>() // events
    val snackbarFlow = _snackbarFlow.asSharedFlow()

    suspend fun showMessage(message: String) {
        _snackbarFlow.emit(message)
    }
}