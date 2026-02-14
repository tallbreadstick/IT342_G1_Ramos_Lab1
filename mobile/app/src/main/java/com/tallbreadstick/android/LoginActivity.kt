package com.tallbreadstick.android

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tallbreadstick.android.models.LoginRequest
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // If token exists, go to profile
        val existing = AuthPreferences.loadToken(this)
        if (!existing.isNullOrEmpty()) {
            AuthPreferences.inMemoryToken = existing
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
            return
        }

        val etUser = findViewById<EditText>(R.id.etUsernameOrEmail)
        val etPass = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegister = findViewById<Button>(R.id.btnGoRegister)

        btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        btnLogin.setOnClickListener {
            val usernameOrEmail = etUser.text.toString().trim()
            val password = etPass.text.toString()
            if (usernameOrEmail.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter credentials", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val resp = ApiClient.authApi.login(LoginRequest(usernameOrEmail, password))
                    // Save token
                    AuthPreferences.saveToken(this@LoginActivity, resp.token)
                    startActivity(Intent(this@LoginActivity, ProfileActivity::class.java))
                    finish()
                } catch (e: Exception) {
                    Toast.makeText(this@LoginActivity, "Login failed: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
