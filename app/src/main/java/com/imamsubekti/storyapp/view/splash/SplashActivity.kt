package com.imamsubekti.storyapp.view.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.imamsubekti.storyapp.databinding.ActivitySplashBinding
import com.imamsubekti.storyapp.view.list.ListActivity
import com.imamsubekti.storyapp.view.welcome.WelcomeActivity
import com.imamsubekti.storyapp.ViewModelFactory

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var model: SplashViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model = ViewModelProvider(this, ViewModelFactory.getInstance(this))[SplashViewModel::class.java]

        checkToken()
    }

    private fun checkToken(){
        model.getToken().observe(this){
            val targetIntent = if (it.isEmpty()) {
                Intent(this, WelcomeActivity::class.java)
            } else {
                Intent(this, ListActivity::class.java)
            }
            startActivity(targetIntent)
            finish()
        }
    }
}