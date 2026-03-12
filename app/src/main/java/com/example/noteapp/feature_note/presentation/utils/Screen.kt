package com.example.noteapp.feature_note.presentation.utils

import com.example.noteapp.feature_note.presentation.signIn.phone.PhoneAuthMode

sealed class Screen (val route: String) {
    object NotesScreen: Screen("notes_screen")
    object AddEditNoteScreen: Screen("add_edit_note_screen")
    object SignInScreen: Screen("sign_in_screen")
    object PhoneSignInScreen: Screen("phone_sign_in_screen"){
        // Helper to include mode in route
        fun createRoute(mode: PhoneAuthMode): String =
            "phone_sign_in_screen/${mode.name}"
    }
}