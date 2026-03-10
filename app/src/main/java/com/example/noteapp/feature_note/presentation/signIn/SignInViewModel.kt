package com.example.noteapp.feature_note.presentation.signIn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.feature_note.domain.repository.DataStoreRepository
import com.example.noteapp.feature_note.domain.use_case.SignInUseCase
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
                    _state.value = state.value.copy(
                        isLoading = true
                    )
                    val result = signInUseCase.googleSignInUseCase(event.idToken)
                    result.onSuccess {
                        dataStoreRepository.saveLoginState(true)
                        _state.value = state.value.copy(
                            isLoading = false,
                            isSuccess = true
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