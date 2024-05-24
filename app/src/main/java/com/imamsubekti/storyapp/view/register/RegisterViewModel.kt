package com.imamsubekti.storyapp.view.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imamsubekti.storyapp.entity.BasicResponse
import com.imamsubekti.storyapp.entity.RegisterRequest
import com.imamsubekti.storyapp.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel: ViewModel() {
    private val apiService = ApiConfig.getApiService()

    private val _status = MutableLiveData<RegisterStatus>()
    val status: LiveData<RegisterStatus> get() = _status

    fun register(name: String, email: String, password: String) {
        val registerRequestData = RegisterRequest(
            name = name,
            email = email,
            password = password
        )

        _status.postValue(RegisterStatus.PROGRESS)

        apiService.register(registerRequestData).enqueue(object: Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if (response.isSuccessful && !response.body()?.error!!) {
                    _status.postValue(RegisterStatus.SUCCESS)
                } else {
                    _status.postValue(RegisterStatus.FAILED)
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                _status.postValue(RegisterStatus.FAILED)
                Log.e("Registration Process", "onFailure: failed to register: ${t.message}" )
            }

        })
    }

    enum class RegisterStatus(val value: Int) {
        SUCCESS(200),
        FAILED(400),
        PROGRESS(100)
    }
}
