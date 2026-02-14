package com.tallbreadstick.android.api

import android.content.Context
import android.util.Log
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import retrofit2.Response

// =========================
// Data Models
// =========================

data class User(
    val id: Int,
    val username: String,
    val email: String,
    val profileImage: String?,
    val dateJoined: String
)

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)

data class LoginRequest(
    val usernameOrEmail: String,
    val password: String
)

data class AuthResponse(
    val token: String,
    val user: User
)

// =========================
// Retrofit API Interface
// =========================

interface ApiService {

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<User>

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>

    @GET("user/me")
    suspend fun getProfile(): Response<User>

    @POST("auth/logout")
    suspend fun logout(): Response<Unit>
}

// =========================
// Token Manager
// =========================

class TokenManager(context: Context) {

    private val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        prefs.edit().putString("authToken", token).apply()
        Log.d("Auth", "Token saved")
    }

    fun getToken(): String? {
        return prefs.getString("authToken", null)
    }

    fun clearToken() {
        prefs.edit().remove("authToken").apply()
        Log.d("Auth", "Token cleared")
    }

    fun isAuthenticated(): Boolean {
        return getToken() != null
    }
}

// =========================
// Retrofit Client Builder
// =========================

object ApiClient {

    private const val BASE_URL = "http://10.0.2.2:8080/api/"
    // 10.0.2.2 = localhost for Android emulator

    fun create(context: Context): ApiService {

        val tokenManager = TokenManager(context)

        val authInterceptor = Interceptor { chain ->
            val original = chain.request()
            val builder = original.newBuilder()

            tokenManager.getToken()?.let { token ->
                builder.addHeader("Authorization", "Bearer $token")
            }

            val request = builder.build()
            chain.proceed(request)
        }

        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Log.d("API_LOG", message)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }
}
