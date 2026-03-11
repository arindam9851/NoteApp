package com.example.noteapp.feature_note.presentation.utils

sealed class Screen (val route: String) {
    object NotesScreen: Screen("notes_screen")
    object AddEditNoteScreen: Screen("add_edit_note_screen")
    object SignInScreen: Screen("sign_in_screen")
    object PhoneSignInScreen: Screen("phone_sign_in_screen")
}