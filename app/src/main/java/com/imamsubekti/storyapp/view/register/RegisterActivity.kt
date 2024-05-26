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

        setupAction()
        observeResponse()
    }

    private fun setupAction() {
        binding.buttonRegisterSubmit.setOnClickListener {
            model.register(
                name = binding.nameForm.text.toString(),
                email = binding.emailForm.text.toString(),
                password = binding.passwordForm.text.toString()
            )
        }
    }

    private fun observeResponse(){
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
                else -> { }
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