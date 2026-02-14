package com.tallbreadstick.android

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tallbreadstick.android.models.User
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val tvUsername = findViewById<TextView>(R.id.tvUsername)
        val tvEmail = findViewById<TextView>(R.id.tvEmail)
        val tvDate = findViewById<TextView>(R.id.tvDateJoined)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        lifecycleScope.launch {
            try {
                val user: User = ApiClient.authApi.getProfile()
                tvUsername.text = user.username
                tvEmail.text = user.email
                tvDate.text = user.dateJoined
            } catch (e: Exception) {
                Toast.makeText(this@ProfileActivity, "Failed to load profile: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

        btnLogout.setOnClickListener {
            lifecycleScope.launch {
                try {
                    ApiClient.authApi.logout()
                } catch (_: Exception) {
                }
                AuthPreferences.clearToken(this@ProfileActivity)
                startActivity(Intent(this@ProfileActivity, LoginActivity::class.java))
                finish()
            }
        }
    }
}
