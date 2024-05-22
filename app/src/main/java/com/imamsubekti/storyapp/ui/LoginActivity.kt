package com.imamsubekti.storyapp.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.imamsubekti.storyapp.R
import com.imamsubekti.storyapp.databinding.ActivityLoginBinding
import com.imamsubekti.storyapp.repository.DataStoreRepository
import com.imamsubekti.storyapp.repository.dataStore
import com.imamsubekti.storyapp.viewmodel.LoginViewModel
import com.imamsubekti.storyapp.viewmodel.ViewModelFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var model: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = DataStoreRepository.getInstance(application.dataStore)
        model = ViewModelProvider(this, ViewModelFactory(pref))[LoginViewModel::class.java]

        binding.buttonLoginSubmit.setOnClickListener {
            val emailField = binding.emailForm.text.toString()
            val passwordField = binding.passwordForm.text.toString()

            model.login(emailField, passwordField)
        }

        model.status.observe(this){
            when(it) {
                LoginViewModel.LoginStatus.SUCCESS -> {
                    showLoading(false)
                    Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
                }
                LoginViewModel.LoginStatus.FAILED -> {
                    showLoading(false)
                    binding.errorMsg.visibility = View.VISIBLE
                    Toast.makeText(this, "failure", Toast.LENGTH_SHORT).show()
                }
                LoginViewModel.LoginStatus.PROGRESS -> {
                    showLoading()
                }
                else -> {
                    // do nothing
                }
            }
        }

        model.getToken().observe(this){
            binding.tokenField.text = it
        }
    }

    private fun showLoading(state: Boolean = true){
        with(binding) {
            if (state) {
                buttonLoginSubmit.text = getString(R.string.loading)
                errorMsg.visibility = View.GONE
            } else {
                buttonLoginSubmit.text = getString(R.string.login)
                errorMsg.visibility = View.GONE
            }
        }
    }
}