package com.example.noteapp.feature_note.presentation.signIn.google

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.noteapp.R
import com.example.noteapp.feature_note.presentation.signIn.google.components.SignInButton
import com.example.noteapp.feature_note.presentation.utils.Screen
import com.example.noteapp.feature_note.presentation.utils.snackbar.AppSnackbarHost
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch
import com.example.noteapp.BuildConfig
import com.example.noteapp.feature_note.presentation.signIn.phone.PhoneAuthMode

@Composable
fun SignInScreen(
    navController: NavController,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val clientId = BuildConfig.GOOGLE_CLIENT_ID
    Log.d("Arindam", "Signin Screen main")

    // Trigger check on first composition
    LaunchedEffect(Unit) {
        viewModel.onEvent(SignInEvent.CheckIfLoggedIn)
        Log.d("Arindam", "Signin Screen from launch effect check login")
    }

    // Navigation
    LaunchedEffect(state.navigationTarget, state.hasNavigated) {
        state.navigationTarget?.let { route ->
            navController.navigate(route) {
                popUpTo(Screen.SignInScreen.route) { inclusive = true }
            }
            viewModel.onNavigated()
        }
        Log.d("Arindam", "Signin Screen from launch effect")
    }

    Scaffold(
        snackbarHost = { AppSnackbarHost(snackbarHostState)
            //TODO Snackbar color change
//        { data ->
//            Snackbar(
//                snackbarData = data,
//                containerColor = Color(0xFF6200EE), // background color
//                contentColor = Color.White, // text color
//                actionColor = Color.Yellow // action button color
//            )
//        }
        }

    ){ padding ->
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(padding)
        ) {

            Image(
                painter = painterResource(id = R.drawable.login_bg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.Start
            ) {

                Text(
                    text = "Sign in",
                    color = Color(0xFFFFD700),
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Sign In With Any Of Your Google Account Or Phone And Set Your Password Through App",
                    color = Color.White.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                SignInButton(
                    text = "Continue with Google",
                    onClick = {

                        scope.launch {

                            try {

                                val credentialManager = CredentialManager.create(context)

                                val googleIdOption = GetGoogleIdOption.Builder()
                                    .setFilterByAuthorizedAccounts(false)
                                    .setServerClientId(
                                        clientId
                                    )
                                    .setAutoSelectEnabled(false)
                                    .build()

                                val request = GetCredentialRequest.Builder()
                                    .addCredentialOption(googleIdOption)
                                    .build()

                                val result = credentialManager.getCredential(
                                    context = context,
                                    request = request
                                )

                                val credential = result.credential

                                if (
                                    credential is CustomCredential &&
                                    credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                                ) {

                                    val googleIdTokenCredential =
                                        GoogleIdTokenCredential.createFrom(credential.data)

                                    val idToken = googleIdTokenCredential.idToken

                                    viewModel.onEvent(
                                        SignInEvent.GoogleSignIn(idToken)
                                    )
                                }

                            } catch (e: Exception) {
                                Log.e("SignInScreen", "Google sign in failed", e)
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                SignInButton(
                    text = "Continue with Phone",
                    onClick = {
                        navController.navigate(
                            Screen.PhoneSignInScreen.createRoute(PhoneAuthMode.SIGN_IN_NEW_USER)
                        ) {
                            popUpTo(Screen.SignInScreen.route) { inclusive = true } // removes SignInScreen
                        }
                    }
                )

                Spacer(modifier = Modifier.height(48.dp))
            }

            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
//        LaunchedEffect(state.isSuccess, state.hasNavigated) {
//            if (state.isSuccess && !state.hasNavigated) {
//                if (!state.isPhoneLinked) {
//                    navController.navigate(Screen.PhoneSignInScreen.route) {
//                        popUpTo(Screen.SignInScreen.route) { inclusive = true }
//                    }
//                } else {
//                    navController.navigate(Screen.NotesScreen.route) {
//                        popUpTo(Screen.SignInScreen.route) { inclusive = true }
//                    }
//                }
//                viewModel.onNavigated()
//            }
//        }
        // Show error
        LaunchedEffect(state.error) {
            state.error?.let { message ->
                scope.launch {
                    snackbarHostState.showSnackbar(message)
                }
            }
        }
    }
}
