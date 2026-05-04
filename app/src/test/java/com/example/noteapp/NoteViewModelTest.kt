package com.example.noteapp

import app.cash.turbine.test
import com.example.noteapp.feature_note.domain.model.Note
import com.example.noteapp.feature_note.domain.repository.DataStoreRepository
import com.example.noteapp.feature_note.domain.use_case.AddNoteUseCase
import com.example.noteapp.feature_note.domain.use_case.DeleteNoteUse
import com.example.noteapp.feature_note.domain.use_case.GetListOfNoteUseCase
import com.example.noteapp.feature_note.domain.use_case.GetNoteUseCase
import com.example.noteapp.feature_note.domain.use_case.NoteUseCases
import com.example.noteapp.feature_note.domain.use_case.ShareNoteUserCase
import com.example.noteapp.feature_note.presentation.notes.NotesEvent
import com.example.noteapp.feature_note.presentation.notes.NotesViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NoteViewModelTest {
    // Rule to replace main dispatcher with test dispatcher
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var addNote: AddNoteUseCase
    private lateinit var deleteNote: DeleteNoteUse
    private lateinit var getListOfNotes: GetListOfNoteUseCase
    private lateinit var shareNote: ShareNoteUserCase
    private lateinit var getNote: GetNoteUseCase

    private lateinit var notesUseCases: NoteUseCases

    private lateinit var note: Note
    private val dataStoreRepository = mockk<DataStoreRepository>(relaxed = true)

    private lateinit var viewModel: NotesViewModel

    //Common setup for all use case

    @Before
    fun setup(){
        addNote = mockk(relaxed = true)
        deleteNote = mockk(relaxed = true)
        getListOfNotes = mockk(relaxed = true)
        shareNote = mockk(relaxed = true)
        getNote = mockk(relaxed = true)
        note = Note(
            title = "Test Note",
            content = "This is a test note",
            timeStamp = System.currentTimeMillis(),
            color = 1,
            userId = "testUser",
        )
        notesUseCases = NoteUseCases(
            addNote = addNote,
            deleteNote = deleteNote,
            getListOfNotes = getListOfNotes,
            shareNote = shareNote,
            getNote = getNote
        )
        viewModel = NotesViewModel(
            noteUseCases = notesUseCases,
            dataStoreRepository = dataStoreRepository
        )
    }

    @Test
    fun `toggle order section should change visibility`() {
        val initial = viewModel.state.value.isOrderSectionVisible
        viewModel.onEvent(NotesEvent.ToggleOrderSection)
        val updated = viewModel.state.value.isOrderSectionVisible
        assert(initial != updated)


    }
    // --------------------------------------------
    // isLoggedIn Flow Test (Turbine)
    // --------------------------------------------
    @Test
    fun `isLoggedIn flow emits correct values`() = runTest {
        coEvery {
            dataStoreRepository.getLoginState()
        } returns flowOf(true, false)
        viewModel = NotesViewModel(notesUseCases, dataStoreRepository)

        viewModel.isLoggedIn.test {
            assert(awaitItem())
            assert(!awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
    // --------------------------------------------
    // Delete Note (Interaction Test)
    // --------------------------------------------
    @Test
    fun `delete note should call delete use case`() = runTest{

        viewModel.onEvent(NotesEvent.DeleteNote(note))
        //run all scheduled coroutines in the test dispatcher until there’s nothing left to run
        advanceUntilIdle()
        //now verify after everything has actually executed
        // Verify that the deleteNote function was called with the correct note
        coVerify { notesUseCases.deleteNote(note) }

    }

    @Test
    fun `restore note should re-added`() = runTest {

        viewModel.onEvent(NotesEvent.DeleteNote(note))

        viewModel.onEvent(NotesEvent.RestoreNote)

        advanceUntilIdle()

        coVerify { notesUseCases.addNote(note) }
    }

    @Test
    fun `restore note should not call without delete`()= runTest {

        viewModel.onEvent(NotesEvent.RestoreNote)
        advanceUntilIdle()

        coVerify(exactly = 0) { notesUseCases.addNote(any()) }
    }

    // --------------------------------------------
    // getNotes Flow → State update
    // --------------------------------------------

    @Test
    fun `get notes should update state`() = runTest {

        val notes = listOf(note)
        coEvery { getListOfNotes(any()) } returns flowOf(notes)
        // 🔥 recreate ViewModel AFTER mocking
        viewModel = NotesViewModel(notesUseCases, dataStoreRepository)
        advanceUntilIdle()

        // Check state updated
        assert(viewModel.state.value.notes == notes)
    }

    // --------------------------------------------
    // Share Note (Flow + State + Interaction)
    // --------------------------------------------
    @Test
    fun `share note should update url and mark note as shared`() = runTest {

        // Mock share flow
        coEvery {
            notesUseCases.shareNote(note)
        } returns flowOf("test_url")


        viewModel.onEvent(NotesEvent.ShareNote(note))

        advanceUntilIdle()

        // Check state updated
        assert(viewModel.state.value.noteShareUrl == "test_url")

        // Check note updated and saved
        //Verify that addNote() was called with a Note that satisfies this condition”
        coVerify {
            notesUseCases.addNote(match {
                it.isShared && it.shareUrl == "test_url"
            })
        }
    }
}



