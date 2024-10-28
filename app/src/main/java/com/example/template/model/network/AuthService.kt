package com.example.template.model.network

import com.example.template.model.entity.auth.AuthRequest
import com.example.template.model.entity.auth.AuthResponse
import com.example.template.model.entity.auth.TokenResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth/google")
    fun googleLogin(
        @Body request: AuthRequest
    ): Call<AuthResponse>

    @POST("auth/refresh")
    fun refreshAccessToken(
        @Body refreshToken: String
    ): Call<TokenResponse>
}