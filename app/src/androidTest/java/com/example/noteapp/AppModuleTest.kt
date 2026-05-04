package com.example.noteapp

import com.example.noteapp.feature_note.data.data_source.NoteDatabase
import com.example.noteapp.feature_note.domain.model.Note
import com.example.noteapp.feature_note.domain.repository.NoteRepository
import com.example.noteapp.feature_note.domain.use_case.NoteUseCases
import com.example.noteapp.feature_note.domain.utils.NoteOrder
import com.example.noteapp.feature_note.domain.utils.OrderType
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import jakarta.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class AppModuleTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject lateinit var noteUseCases: NoteUseCases
    @Inject lateinit var database: NoteDatabase
    @Inject lateinit var repository: NoteRepository


    @Before
    fun setup(){
        hiltRule.inject()
        database.clearAllTables()
    }

    @Test
    fun insert_and_read_note_from_real_db() = runTest {

        val note = Note(
            title = "Hilt Test",
            content = "Testing real DB",
            timeStamp = System.currentTimeMillis(),
            color = 1,
            userId = "testUser"
        )

        // Insert note using UseCase
        noteUseCases.addNote(note)

        // Fetch notes
        val notes = noteUseCases
            .getListOfNotes(NoteOrder.Date(OrderType.Descending))
            .first()

        // Verify
        assert(notes.isNotEmpty())
        assert(notes.any { it.title == "Hilt Test" })
    }
}