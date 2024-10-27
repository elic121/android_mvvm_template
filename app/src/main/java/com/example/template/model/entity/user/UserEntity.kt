package com.example.template.model.entity.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserEntity(
    val id: String,
    val email: String,
    val name: String
): Parcelable
