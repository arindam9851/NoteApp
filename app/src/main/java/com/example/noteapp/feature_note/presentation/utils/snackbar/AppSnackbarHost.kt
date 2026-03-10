package com.example.noteapp.feature_note.presentation.utils.snackbar

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch


@Composable
fun AppSnackbarHost(snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }) {
    val scope = rememberCoroutineScope()

    // Launch when snackbarFlow emits
    LaunchedEffect(Unit) {
        SnackBarManager.snackbarFlow.collect { message ->
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = {data ->
            Snackbar(
                snackbarData = data,
//                containerColor = Color(0xFF6200EE), // background color
//                contentColor = Color.White, // text color
//                actionColor = Color.Yellow // action button color
            )
        }
    )
}