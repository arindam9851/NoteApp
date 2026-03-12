package com.example.noteapp.feature_note.presentation

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.noteapp.feature_note.domain.repository.DataStoreRepository
import com.example.noteapp.feature_note.presentation.add_edit_note.AddEditNoteScreen
import com.example.noteapp.feature_note.presentation.notes.NotesScreen
import com.example.noteapp.feature_note.presentation.signIn.google.SignInScreen
import com.example.noteapp.feature_note.presentation.signIn.phone.PhoneAuthMode
import com.example.noteapp.feature_note.presentation.signIn.phone.PhoneSignInScreen
import com.example.noteapp.feature_note.presentation.utils.Screen
import com.example.noteapp.ui.theme.NoteAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var dataStoreRepository: DataStoreRepository

    @SuppressLint("ContextCastToActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NoteAppTheme {
                val navController = rememberNavController()
                val isLoggedIn by dataStoreRepository
                    .getLoginState()
                    .collectAsStateWithLifecycle(initialValue = false)

                val startDestination =
                    if (isLoggedIn)
                        Screen.NotesScreen.route
                    else
                        Screen.SignInScreen.route

                NavHost(
                    navController = navController,
                    startDestination = startDestination
                ) {
                    composable(route = Screen.SignInScreen.route) {
                        SignInScreen(navController = navController)
                    }
                    composable(
                        route = "phone_sign_in_screen/{mode}",// include mode placeholder
                        arguments = listOf(navArgument("mode") { type = NavType.StringType })
                    ) {backStackEntry->
                        val modeArg = backStackEntry.arguments?.getString("mode")
                            ?: PhoneAuthMode.SIGN_IN_NEW_USER.name
                        val mode = PhoneAuthMode.valueOf(modeArg)
                        // Pass navController or a callback for when verification is done
                        PhoneSignInScreen(
                            activity = LocalContext.current as Activity,
                            viewModel = hiltViewModel(),
                            mode = mode,
                            navController = navController,
                        )
                    }
                    composable(route = Screen.NotesScreen.route) {
                        NotesScreen(navController = navController)
                    }
                    composable(
                        route = Screen.AddEditNoteScreen.route +
                                "?noteId={noteId}&noteColor={noteColor}",
                        arguments = listOf(
                            navArgument(
                                name = "noteId"
                            ) {
                                type = NavType.IntType
                                defaultValue = -1
                            },
                            navArgument(
                                name = "noteColor"
                            ) {
                                type = NavType.IntType
                                defaultValue = -1
                            },
                        )
                    ) {
                        val color = it.arguments?.getInt("noteColor") ?: -1
                        AddEditNoteScreen(
                            navController = navController,
                            noteColor = color
                        )
                    }
                }
            }
        }
    }
}
