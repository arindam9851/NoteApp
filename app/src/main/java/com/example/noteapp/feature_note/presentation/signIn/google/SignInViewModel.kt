package com.example.noteapp.feature_note.presentation.signIn.google

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.feature_note.domain.repository.DataStoreRepository
import com.example.noteapp.feature_note.domain.use_case.SignInUseCase
import com.example.noteapp.feature_note.presentation.signIn.phone.PhoneAuthMode
import com.example.noteapp.feature_note.presentation.utils.Screen
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val dataStoreRepository: DataStoreRepository
): ViewModel(){

    private val _state = MutableStateFlow(SignInState())
    val state: StateFlow<SignInState> = _state

    fun onEvent(event: SignInEvent){
        when(event){
            is SignInEvent.GoogleSignIn -> {
                viewModelScope.launch {
                    _state.value = state.value.copy(isLoading = true)
                    val result = signInUseCase.googleSignInUseCase(event.idToken)

                    result.onSuccess {
                        val currentUser = FirebaseAuth.getInstance().currentUser
                        val phoneLinked = !currentUser?.phoneNumber.isNullOrEmpty()

                        val nextScreen = if (phoneLinked) {
                            Screen.NotesScreen.route // Already has phone linked
                        } else {
                            Screen.PhoneSignInScreen.createRoute(PhoneAuthMode.LINK_TO_EXISTING_USER)
                        }
                        // If phone linked then open note screen
                        if(phoneLinked)
                        {
                            dataStoreRepository.saveLoginState(true)
                        }

                        _state.value = state.value.copy(
                            isLoading = false,
                            isSuccess = true,
                            isPhoneLinked = phoneLinked,
                            hasNavigated = false,
                            navigationTarget = nextScreen
                        )
                    }.onFailure {
                        _state.value = state.value.copy(
                            isLoading = false,
                            error = it.message
                        )
                    }
                }
            }

            SignInEvent.CheckIfLoggedIn -> {
                viewModelScope.launch {
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    val target = if (currentUser != null) {
                        Screen.NotesScreen.route
                    } else null

                    _state.value = state.value.copy(
                        isSuccess = currentUser != null,
                        hasNavigated = false,
                        navigationTarget = target
                    )
                }
            }
        }
    }
    fun onNavigated() {
        _state.value = state.value.copy(hasNavigated = true)
    }
}