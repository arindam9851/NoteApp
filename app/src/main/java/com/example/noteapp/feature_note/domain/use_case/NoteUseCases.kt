package com.example.noteapp.feature_note.domain.use_case

data class NoteUseCases(
    val getListOfNotes: GetListOfNoteUseCase,
    val deleteNote: DeleteNoteUse,
    val addNote: AddNoteUseCase,
    val getNote: GetNoteUseCase,
)
