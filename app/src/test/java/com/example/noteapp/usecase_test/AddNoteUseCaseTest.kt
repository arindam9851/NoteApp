package com.example.noteapp.usecase_test

import com.example.noteapp.feature_note.domain.model.InvalidNoteException
import com.example.noteapp.feature_note.domain.model.Note
import com.example.noteapp.feature_note.domain.repository.NoteRepository
import com.example.noteapp.feature_note.domain.use_case.AddNoteUseCase
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddNoteUseCaseTest {
    private lateinit var repository: NoteRepository
    private lateinit var addNoteUseCase: AddNoteUseCase
    @Before
    fun setup() {
        repository = mockk(relaxed = true)
        addNoteUseCase = AddNoteUseCase(repository)
    }

    // --------------------------------------------
    // SUCCESS CASE
    // --------------------------------------------
    @Test
    fun `valid note should call repository`() = runTest {
        val note = Note(
            title = "Title",
            content = "Content",
            timeStamp = 0,
            color = 1,
            userId = "user"
        )

        addNoteUseCase(note)

        coVerify { repository.insertNote(note) }
    }

    // --------------------------------------------
    // EMPTY TITLE
    // --------------------------------------------
    @Test
    fun `empty title should throw exception`() = runTest {
        val note = Note(
            title = "",
            content = "Content",
            timeStamp = 0,
            color = 1,
            userId = "user"
        )

        try {
            addNoteUseCase(note)
            assert(false) // should not reach here
        } catch (e: InvalidNoteException) {
            assert(e.message == "The title of the note can't be empty.")
        }

        coVerify(exactly = 0) { repository.insertNote(any()) }
    }

    // --------------------------------------------
    // EMPTY CONTENT
    // --------------------------------------------
    @Test
    fun `empty content should throw exception`() = runTest {
        val note = Note(
            title = "Title",
            content = "",
            timeStamp = 0,
            color = 1,
            userId = "user"
        )

        try {
            addNoteUseCase(note)
            assert(false)
        } catch (e: InvalidNoteException) {
            assert(e.message == "The content of the note can't be empty.")
        }

        coVerify(exactly = 0) { repository.insertNote(any()) }
    }
}