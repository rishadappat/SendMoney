package com.appat.sendmoney.ui.vewmodels

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.appat.sendmoney.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

private const val defaultUsername = "testuser"
private const val defaultPassword = "password123"

@HiltViewModel
class LoginScreenViewModel @Inject constructor(): ViewModel() {


    private val _uiState = MutableStateFlow(LoginScreenState())
    val uiState = _uiState.asStateFlow()


    fun performLogin(): Boolean {
        if (_uiState.value.username.isEmpty()) {
            setErrorMessage(R.string.empty_username)
            return false
        }
        if (_uiState.value.password.isEmpty()) {
            setErrorMessage(R.string.empty_password)
            return false
        }
        if (_uiState.value.username != defaultUsername) {
            setErrorMessage(R.string.invalid_username)
            return false
        }
        if (_uiState.value.password != defaultPassword) {
            setErrorMessage(R.string.invalid_password)
            return false
        }
        setErrorMessage(-1)
        return true
    }

    fun setUsername(newUsername: String) {
        _uiState.update { currentState ->
            currentState.copy(
                username = newUsername
            )
        }
    }

    fun setPassword(newPassword: String) {
        _uiState.update { currentState ->
            currentState.copy(
                password = newPassword
            )
        }
    }

    fun setErrorMessage(@StringRes message: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                errorMessage = message
            )
        }
    }
}

data class LoginScreenState(
    val username: String = "",
    val password: String = "",
    @StringRes val errorMessage: Int = -1
)