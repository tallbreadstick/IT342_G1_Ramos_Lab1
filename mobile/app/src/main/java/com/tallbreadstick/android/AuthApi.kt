package com.tallbreadstick.android

import com.tallbreadstick.android.models.AuthResponse
import com.tallbreadstick.android.models.LoginRequest
import com.tallbreadstick.android.models.RegisterRequest
import com.tallbreadstick.android.models.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): User

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("auth/logout")
    suspend fun logout(): Unit

    @GET("user/me")
    suspend fun getProfile(): User
}
