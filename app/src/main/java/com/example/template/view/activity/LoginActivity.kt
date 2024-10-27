package com.example.template.view.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.GetCredentialInterruptedException
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.lifecycleScope
import com.example.template.BuildConfig
import com.example.template.databinding.ActivityLoginBinding
import com.example.template.util.handleState
import com.example.template.util.toast
import com.example.template.view.base.BaseActivity
import com.example.template.viewmodel.AuthViewModel
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {
    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var credentialManager: CredentialManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        credentialManager = CredentialManager.create(this)
        updateUI()
    }

    private fun updateUI() {
        binding.btnLogin.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        lifecycleScope.launch {
            try {
                // Google Sign In 옵션 설정
                val googleIdOption = getGoogleIdOption()

                // 요청 생성
                val request: GetCredentialRequest = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                // 자격 증명 요청
                val result = credentialManager.getCredential(
                    request = request,
                    context = this@LoginActivity
                )

                // 결과 처리
                when (val credential = result.credential) {
                    is CustomCredential -> {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

                        googleLogin(googleIdTokenCredential.idToken)
                    }
                    else -> {
                        Timber.e("Unexpected credential type")
                    }
                }
            } catch (e: GetCredentialException) {
                handleCredentialException(e)
            }
        }
    }

    private fun getGoogleIdOption(): GetGoogleIdOption {
        return GetGoogleIdOption.Builder()
            .setServerClientId(BuildConfig.SERVER_CLIENT_ID)
            .setFilterByAuthorizedAccounts(false)
            .setAutoSelectEnabled(true)
            .setNonce(null)
            .build()
    }

    private fun googleLogin(idToken: String) {
        Timber.d("idToken : $idToken")
        authViewModel.googleLogin(idToken)
        authViewModel.authResponse.observe(this@LoginActivity) {state ->
            handleState(
                state,
                onLoading = {},
                onSuccess = { authResponse -> Timber.d(authResponse.toString())},
                onError = { errorMessage -> Timber.e(errorMessage)}
            )
        }
    }

    private fun handleCredentialException(e: GetCredentialException) {
        val message = when (e) {
            is NoCredentialException -> "No credentials available"
            is GetCredentialCancellationException -> "Sign in cancelled"
            is GetCredentialInterruptedException -> "Sign in interrupted"
            else -> "Sign in failed: ${e.message}"
        }
        Timber.d(message)
    }
}