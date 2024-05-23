package com.imamsubekti.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.imamsubekti.storyapp.entity.DetailStoryResponse
import com.imamsubekti.storyapp.network.ApiConfig
import com.imamsubekti.storyapp.repository.DataStoreRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(private val pref: DataStoreRepository): ViewModel() {
    private val apiService = ApiConfig.getApiService()

    private val _storyDetail = MutableLiveData<DetailStoryResponse>()
    val storyDetail: LiveData<DetailStoryResponse> get() = _storyDetail

    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }

    fun updateDetail(token: String, id: String) {
        apiService.getStoryById("Bearer $token", id).enqueue(object : Callback<DetailStoryResponse>{
            override fun onResponse(
                call: Call<DetailStoryResponse>,
                response: Response<DetailStoryResponse>
            ) {
                if (response.isSuccessful && !response.body()?.error!!) {
                    _storyDetail.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
                // do nothing
            }

        })
    }
}