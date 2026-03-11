package com.example.noteapp.feature_note.presentation.signIn.phone

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.feature_note.domain.use_case.SignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhoneSignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase
): ViewModel() {

    private val _state = MutableStateFlow(PhoneSignInState())
    val state: StateFlow<PhoneSignInState> = _state

    fun onEvent(event: PhoneSignInEvent) {
        when (event) {

            is PhoneSignInEvent.EnterPhone -> {
                _state.value = state.value.copy(phone = event.phone)
            }

            is PhoneSignInEvent.EnterOtp -> {
                _state.value = state.value.copy(otp = event.otp)
            }

            is PhoneSignInEvent.SendOtp -> {
                val phoneNumber = state.value.phone
                if (phoneNumber.isBlank()) return

                _state.value = state.value.copy(isLoading = true, error = null)

                viewModelScope.launch {
                    val result = signInUseCase.phoneSignInUseCase.invokeSendOtp(phoneNumber, event.activity)
                    result.onSuccess { verificationId ->
                        _state.value = state.value.copy(
                            isLoading = false,
                            verificationId = verificationId
                        )
                    }.onFailure {
                        _state.value = state.value.copy(
                            isLoading = false,
                            error = it.message
                        )
                    }
                }
            }

            PhoneSignInEvent.VerifyOtp -> {
                val verificationId = state.value.verificationId
                val otp = state.value.otp
                if (verificationId.isNullOrBlank() || otp.isBlank()) return

                _state.value = state.value.copy(isLoading = true, error = null)

                viewModelScope.launch {
                    val result = signInUseCase.phoneSignInUseCase.invokeVerifyOtp(verificationId, otp)
                    result.onSuccess {
                        _state.value = state.value.copy(
                            isLoading = false,
                            isVerified = true
                        )
                    }.onFailure {
                        _state.value = state.value.copy(
                            isLoading = false,
                            error = it.message
                        )
                    }
                }
            }
        }
    }

}