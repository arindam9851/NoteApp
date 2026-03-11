package com.example.noteapp.feature_note.presentation.signIn.phone

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun PhoneSignInScreen(
    viewModel: PhoneSignInViewModel = hiltViewModel(),
    activity: Activity,
    onVerified: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {

        if (state.verificationId == null) {
            OutlinedTextField(
                value = state.phone,
                onValueChange = { viewModel.onEvent(PhoneSignInEvent.EnterPhone(it)) },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { viewModel.onEvent(PhoneSignInEvent.SendOtp(activity)) },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Send OTP") }
        } else {
            OutlinedTextField(
                value = state.otp,
                onValueChange = { viewModel.onEvent(PhoneSignInEvent.EnterOtp(it)) },
                label = { Text("Enter OTP") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { viewModel.onEvent(PhoneSignInEvent.VerifyOtp) },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Verify OTP") }
        }

        if (state.isLoading) {
            Spacer(Modifier.height(16.dp))
            CircularProgressIndicator()
        }

        state.error?.let {
            Spacer(Modifier.height(8.dp))
            Text(it)
        }

        if (state.isVerified) {
            LaunchedEffect(Unit) {
                onVerified()
            }
        }
    }
}