package com.imamsubekti.storyapp.view.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.imamsubekti.storyapp.databinding.ActivitySplashBinding
import com.imamsubekti.storyapp.repository.DataStoreRepository
import com.imamsubekti.storyapp.repository.dataStore
import com.imamsubekti.storyapp.view.list.ListActivity
import com.imamsubekti.storyapp.view.welcome.WelcomeActivity
import com.imamsubekti.storyapp.config.ViewModelFactory

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var model: SplashViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = DataStoreRepository.getInstance(application.dataStore)
        model = ViewModelProvider(this, ViewModelFactory(pref))[SplashViewModel::class.java]

        model.getToken().observe(this){
            val targetIntent = if (it == "") {
                Intent(this, WelcomeActivity::class.java)
            } else {
                Intent(this, ListActivity::class.java)
            }
            startActivity(targetIntent)
            finish()
        }
    }
}