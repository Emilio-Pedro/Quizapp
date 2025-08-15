package com.example.quizzapp.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizzapp.data.model.User
import com.example.quizzapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val email: String = "",
    val pass: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val loginSuccess: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {


    private val _uiState = MutableStateFlow(LoginUiState())

    val uiState = _uiState.asStateFlow()

    fun onEmailChange(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
    }

    fun onPasswordChange(newPass: String) {
        _uiState.update { it.copy(pass = newPass) }
    }

    fun login() {

        if (_uiState.value.email.isBlank() || _uiState.value.pass.isBlank()) {
            _uiState.update { it.copy(error = "Email e password não podem estar vazios.") }
            return
        }


        viewModelScope.launch {
            try {

                _uiState.update { it.copy(isLoading = true, error = null) }

                repository.login(_uiState.value.email, _uiState.value.pass)

                _uiState.update { it.copy(isLoading = false, loginSuccess = true) }
            } catch (e: Exception) {

                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

   fun register() {
        if (_uiState.value.email.isBlank() || _uiState.value.pass.isBlank()) {
            _uiState.update { it.copy(error = "Email e password não podem estar vazios.") }
            return
        }

        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = null) }
                repository.register(_uiState.value.email, _uiState.value.pass)
                _uiState.update { it.copy(isLoading = false, loginSuccess = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}