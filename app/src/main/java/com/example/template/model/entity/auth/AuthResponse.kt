package com.example.template.model.entity.auth

import com.example.template.model.entity.user.UserEntity
import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("access_token")
    val accessToken: String,

    @SerializedName("refresh_token")
    val refreshToken: String?,

    @SerializedName("user")
    val user: UserEntity
)
