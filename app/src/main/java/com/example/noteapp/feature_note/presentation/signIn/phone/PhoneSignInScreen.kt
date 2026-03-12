package com.example.noteapp.feature_note.presentation.signIn.phone

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.noteapp.feature_note.presentation.signIn.phone.components.OtpInput
import com.example.noteapp.feature_note.presentation.signIn.phone.components.PhoneNumberInput
import com.example.noteapp.feature_note.presentation.utils.Screen
import com.example.noteapp.feature_note.presentation.utils.progress.LoadingOverlay

@Composable
fun PhoneSignInScreen(
    navController: NavController,
    mode: PhoneAuthMode = PhoneAuthMode.LINK_TO_EXISTING_USER,
    viewModel: PhoneSignInViewModel = hiltViewModel(),
    activity: Activity,
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(state.navigationTarget, state.hasNavigated) {
        state.navigationTarget?.let { route ->
            navController.navigate(route) {
                popUpTo(Screen.SignInScreen.route) { inclusive = true }
            }
            viewModel.onNavigated()
        }
    }
    val title = if (state.verificationId == null) {
        when (mode) {
            PhoneAuthMode.LINK_TO_EXISTING_USER -> "Add phone number"
            PhoneAuthMode.SIGN_IN_NEW_USER -> "Sign in with phone number"
        }
    } else {
        "Enter verification code"
    }

    val subtitle = if (state.verificationId == null) {
        when (mode) {
            PhoneAuthMode.LINK_TO_EXISTING_USER ->
                "Input a phone number to link your account"
            PhoneAuthMode.SIGN_IN_NEW_USER ->
                "Enter your phone number to sign in"
        }
    } else {
        "Enter the 6-digit code sent to your phone"
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Scaffold(
            bottomBar = {
                Button(
                    onClick = {
                        if (state.verificationId == null) {
                            viewModel.onEvent(PhoneSignInEvent.SendOtp(activity))
                        } else {
                            if (mode == PhoneAuthMode.LINK_TO_EXISTING_USER) {
                                viewModel.onEvent(PhoneSignInEvent.VerifyOtp)
                            }
                            else{
                                viewModel.onEvent(PhoneSignInEvent.VerifyOtpForNewUser)
                            }

                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 56.dp, start = 16.dp, end = 16.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text("Continue")
                }
            }
        ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 20.dp)
            ) {

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = subtitle,
                    color = Color.Gray,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                if (state.verificationId == null) {
                    PhoneNumberInput(
                        onPhoneChange = {
                            viewModel.onEvent(PhoneSignInEvent.EnterPhone(it))
                        }
                    )
                } else {
                    OtpInput(
                        onOtpComplete = {
                            viewModel.onEvent(PhoneSignInEvent.EnterOtp(it))
                        }
                    )
                }
            }
        }
        LoadingOverlay(isVisible = state.isLoading)
    }

    state.error?.let {
        Text(it)
    }

}


