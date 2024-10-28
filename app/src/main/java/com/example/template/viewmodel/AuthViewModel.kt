package com.example.template.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.template.model.entity.ResultState
import com.example.template.model.entity.auth.AuthResponse
import com.example.template.model.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _authResponse = MutableLiveData<ResultState<AuthResponse>>()
    val authResponse: LiveData<ResultState<AuthResponse>> = _authResponse

    fun googleLogin(idToken: String) {
        viewModelScope.launch {
            _authResponse.value = ResultState.Loading

            val result = authRepository.googleLogin(idToken)
            _authResponse.value = if (result.isSuccess) {
                ResultState.Success(result.getOrNull()!!)
            } else {
                ResultState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }
}