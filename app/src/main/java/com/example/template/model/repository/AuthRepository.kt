package com.example.template.model.repository

import com.example.template.model.entity.auth.AuthRequest
import com.example.template.model.entity.auth.AuthResponse
import com.example.template.model.entity.auth.TokenResponse
import com.example.template.model.network.AuthService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authService: AuthService,
    private val dataStoreRepository: DataStoreRepository
) {
    suspend fun googleLogin(idToken: String): Result<AuthResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = authService.googleLogin(AuthRequest(idToken)).execute()

                if (response.isSuccessful) {
                    response.body()?.let { authResponse ->
                        dataStoreRepository.saveTokens(
                            authResponse.accessToken,
                            authResponse.refreshToken
                        )
                        Result.success(authResponse)
                    } ?: Result.failure(Exception("Empty response body"))
                } else {
                    Result.failure(Exception(response.errorBody()?.string() ?: "Unknown error"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun refreshAccessToken(refreshToken: String): Result<TokenResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = authService.refreshAccessToken(refreshToken).execute()

                if (response.isSuccessful) {
                    response.body()?.let { token ->
                        dataStoreRepository.saveTokens(token.accessToken)
                        Result.success(TokenResponse(token.accessToken))
                    } ?: Result.failure(Exception("Empty response body"))
                } else {
                    Result.failure(Exception(response.errorBody()?.string() ?: "Unknown error"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
