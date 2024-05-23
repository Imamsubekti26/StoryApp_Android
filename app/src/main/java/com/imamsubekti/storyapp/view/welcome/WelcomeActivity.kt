package com.imamsubekti.storyapp.view.welcome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.imamsubekti.storyapp.databinding.ActivityWelcomeBinding
import com.imamsubekti.storyapp.view.login.LoginActivity
import com.imamsubekti.storyapp.view.register.RegisterActivity

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerButton.setOnClickListener {
            val toRegister = Intent(this, RegisterActivity::class.java)
            startActivity(toRegister)
        }

        binding.loginButton.setOnClickListener {
            val toRegister = Intent(this, LoginActivity::class.java)
            startActivity(toRegister)
        }
    }
}