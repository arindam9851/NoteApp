package com.example.noteapp

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.noteapp.feature_note.domain.model.InvalidNoteException
import com.example.noteapp.feature_note.domain.model.Note
import com.example.noteapp.feature_note.domain.repository.AuthProvider
import com.example.noteapp.feature_note.domain.use_case.AddNoteUseCase
import com.example.noteapp.feature_note.domain.use_case.DeleteNoteUse
import com.example.noteapp.feature_note.domain.use_case.GetListOfNoteUseCase
import com.example.noteapp.feature_note.domain.use_case.GetNoteUseCase
import com.example.noteapp.feature_note.domain.use_case.NoteUseCases
import com.example.noteapp.feature_note.domain.use_case.ShareNoteUserCase
import com.example.noteapp.feature_note.presentation.add_edit_note.AddEditNoteEvent
import com.example.noteapp.feature_note.presentation.add_edit_note.AddEditNoteViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class AddEditNoteViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private lateinit var addNote: AddNoteUseCase
    private lateinit var deleteNote: DeleteNoteUse
    private lateinit var getListOfNotes: GetListOfNoteUseCase
    private lateinit var shareNote: ShareNoteUserCase
    private lateinit var getNote: GetNoteUseCase

    private lateinit var notesUseCases: NoteUseCases

    private lateinit var viewModel: AddEditNoteViewModel
    private lateinit var authProvider: AuthProvider

    @Before
    fun setup(){
        addNote = mockk(relaxed = true)
        deleteNote = mockk(relaxed = true)
        getListOfNotes = mockk(relaxed = true)
        shareNote = mockk(relaxed = true)
        getNote = mockk(relaxed = true)
        authProvider = mockk()
        every { authProvider.getUserId() } returns "test_user"
        notesUseCases = NoteUseCases(
            addNote = addNote,
            deleteNote = deleteNote,
            getListOfNotes = getListOfNotes,
            shareNote = shareNote,
            getNote = getNote
        )
    }
    // --------------------------------------------
    // HELPER
    // --------------------------------------------
    private fun createViewModel(noteId: String? = null): AddEditNoteViewModel {
        val savedStateHandle = SavedStateHandle(
            noteId?.let { mapOf("noteId" to it) } ?: emptyMap()
        )

        return AddEditNoteViewModel(
            noteUseCases = notesUseCases,
            authProvider = authProvider,
            savedStateHandle = savedStateHandle
        )
    }

    @Test
    fun `init with noteID should load note`() = runTest {
        val note = Note(
            id = "1",
            title = "Title",
            content = "Content",
            color = 123,
            timeStamp = 0,
            userId = "user"
        )

        coEvery { getNote("1") } returns note

        viewModel = createViewModel(noteId = "1")

        advanceUntilIdle()

        assert(viewModel.noteTitle.value.text == "Title")
        assert(viewModel.noteContent.value.text == "Content")
        assert(viewModel.noteColor.value == 123)
    }

    @Test
    fun `enter title should update title state`() {
        viewModel = createViewModel()
        viewModel.onEvent(AddEditNoteEvent.EnteredTitle("New Title"))
        assert(viewModel.noteTitle.value.text == "New Title")

    }

    @Test
    fun `save new note should call addNote and emit success`() = runTest {
        viewModel = createViewModel()

        viewModel.onEvent(AddEditNoteEvent.EnteredTitle("Title"))
        viewModel.onEvent(AddEditNoteEvent.EnteredContent("Content"))

        viewModel.eventFlow.test {
            viewModel.onEvent(AddEditNoteEvent.SaveNote)

            advanceUntilIdle()

            coVerify { addNote(any()) }

            val event = awaitItem()
            assert(event is AddEditNoteViewModel.UiEvent.SaveNote)
        }
    }

    @Test
    fun `save existing note should update and share`() = runTest {

        val note = Note(
            id = "1",
            title = "Old",
            content = "Old",
            color = 1,
            timeStamp = 0,
            userId = "user"
        )

        coEvery { getNote("1") } returns note
        coEvery { shareNote(any()) } returns flowOf("url")

        viewModel = createViewModel(noteId = "1")

        advanceUntilIdle()

        viewModel.onEvent(AddEditNoteEvent.SaveNote)

        advanceUntilIdle()

        coVerify(atLeast = 2) { addNote(any()) }
        coVerify { shareNote(any()) }
    }

    // --------------------------------------------
    // ERROR CASE
    // --------------------------------------------
    @Test
    fun `save note error should emit snackbar`() = runTest {
        coEvery { addNote(any()) } throws InvalidNoteException("Error")

        viewModel = createViewModel()

        viewModel.eventFlow.test {
            viewModel.onEvent(AddEditNoteEvent.SaveNote)

            val event = awaitItem()

            assert(event is AddEditNoteViewModel.UiEvent.ShowSnackbar)
        }
    }
}