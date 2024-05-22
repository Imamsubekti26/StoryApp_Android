package com.imamsubekti.storyapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.imamsubekti.storyapp.entity.LoginRequest
import com.imamsubekti.storyapp.entity.LoginResponse
import com.imamsubekti.storyapp.network.ApiConfig
import com.imamsubekti.storyapp.repository.DataStoreRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: DataStoreRepository): ViewModel() {
    private val apiService = ApiConfig.getApiService()

    private val _status = MutableLiveData<LoginViewModel.LoginStatus>()
    val status: LiveData<LoginViewModel.LoginStatus> get() = _status

    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }
    private fun setToken(token: String){
        viewModelScope.launch {
            pref.setToken(token)
        }
    }

    fun login(email: String, password: String){
        val loginRequestData = LoginRequest(
            email = email,
            password = password
        )

        _status.postValue(LoginStatus.PROGRESS)

        apiService.login(loginRequestData).enqueue(object: Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful && !response.body()?.error!!) {
                    val token = response.body()?.loginResult?.token.toString()
                    _status.postValue(LoginStatus.SUCCESS)
                    setToken(token as String)
                } else {
                    _status.postValue(LoginStatus.FAILED)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _status.postValue(LoginStatus.FAILED)
            }

        })
    }

    enum class LoginStatus(val value: Int) {
        SUCCESS(200),
        FAILED(400),
        PROGRESS(100)
    }
}