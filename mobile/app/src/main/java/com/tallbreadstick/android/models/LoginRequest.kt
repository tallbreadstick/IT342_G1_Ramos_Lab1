package com.tallbreadstick.android.models

data class LoginRequest(
    val usernameOrEmail: String,
    val password: String
)
