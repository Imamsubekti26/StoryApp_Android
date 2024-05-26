package com.imamsubekti.storyapp.view.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.imamsubekti.storyapp.R
import com.imamsubekti.storyapp.databinding.ActivityLoginBinding
import com.imamsubekti.storyapp.view.list.ListActivity
import com.imamsubekti.storyapp.ViewModelFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var model: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model = ViewModelProvider(this, ViewModelFactory.getInstance(this))[LoginViewModel::class.java]

        setupAction()
        observeResponse()
    }

    private fun setupAction(){
        binding.buttonLoginSubmit.setOnClickListener {
            model.login(
                email = binding.emailForm.text.toString(),
                password = binding.passwordForm.text.toString()
            )
        }
    }

    private fun observeResponse(){
        model.status.observe(this){
            when(it) {
                LoginViewModel.LoginStatus.SUCCESS -> {
                    showLoading(false)
                    val toList = Intent(this, ListActivity::class.java)
                    toList.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(toList)
                }
                LoginViewModel.LoginStatus.FAILED -> {
                    showLoading(false)
                    binding.errorMsg.visibility = View.VISIBLE
                    Toast.makeText(this, R.string.failed_to_login, Toast.LENGTH_SHORT).show()
                }
                LoginViewModel.LoginStatus.PROGRESS -> {
                    showLoading()
                }
                else -> { }
            }
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