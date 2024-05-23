package com.imamsubekti.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.imamsubekti.storyapp.entity.AllStoriesResponse
import com.imamsubekti.storyapp.network.ApiConfig
import com.imamsubekti.storyapp.repository.DataStoreRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListViewModel(private val pref: DataStoreRepository): ViewModel() {
    private val apiService = ApiConfig.getApiService()

    private val _listStory = MutableLiveData<AllStoriesResponse>()
    val listStory:LiveData<AllStoriesResponse> get() = _listStory

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }

    fun removeToken() {
        viewModelScope.launch {
            pref.setToken("")
        }
    }

    fun updateList(token: String){
        _error.postValue("")

        apiService.getStories("Bearer $token", 1, 20).enqueue(object: Callback<AllStoriesResponse>{
            override fun onResponse(
                call: Call<AllStoriesResponse>,
                response: Response<AllStoriesResponse>
            ) {
                if (response.isSuccessful && !response.body()?.error!!) {
                    _listStory.postValue(response.body())
                } else {
                    _error.postValue(response.body()?.message!!)
                }
            }

            override fun onFailure(call: Call<AllStoriesResponse>, t: Throwable) {
                _error.postValue(t.message)
            }

        })
    }
}