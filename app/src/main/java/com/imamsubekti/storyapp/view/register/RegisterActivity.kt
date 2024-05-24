package com.imamsubekti.storyapp.view.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.imamsubekti.storyapp.R
import com.imamsubekti.storyapp.databinding.ActivityRegisterBinding
import com.imamsubekti.storyapp.view.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var model: RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model = ViewModelProvider(this)[RegisterViewModel::class.java]

        binding.buttonRegisterSubmit.setOnClickListener {
            val nameField = binding.nameForm.text.toString()
            val emailField = binding.emailForm.text.toString()
            val passwordField = binding.passwordForm.text.toString()
            model.register(nameField, emailField, passwordField)
        }

        model.status.observe(this) {
            when(it) {
                RegisterViewModel.RegisterStatus.SUCCESS -> {
                    showLoading(false)
                    val toLogin = Intent(this, LoginActivity::class.java)
                    startActivity(toLogin)
                    finish()
                }
                RegisterViewModel.RegisterStatus.FAILED -> {
                    showLoading(false)
                    binding.errorMsg.visibility = View.VISIBLE
                    Toast.makeText(this, R.string.failed_to_register, Toast.LENGTH_SHORT).show()
                }
                RegisterViewModel.RegisterStatus.PROGRESS -> {
                    showLoading()
                }
                else -> {
                    // do nothing
                }
            }
        }
    }

    private fun showLoading(state: Boolean = true){
        with(binding) {
            if (state) {
                buttonRegisterSubmit.text = getString(R.string.loading)
                errorMsg.visibility = View.GONE
            } else {
                buttonRegisterSubmit.text = getString(R.string.register)
                errorMsg.visibility = View.GONE
            }
        }
    }
}