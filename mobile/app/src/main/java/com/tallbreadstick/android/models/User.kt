package com.tallbreadstick.android.models

data class User(
    val id: Int,
    val username: String,
    val email: String,
    val profileImage: String?,
    val dateJoined: String
)
