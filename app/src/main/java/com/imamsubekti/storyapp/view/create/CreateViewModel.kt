package com.imamsubekti.storyapp.view.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.imamsubekti.storyapp.network.ApiConfig
import com.imamsubekti.storyapp.repository.DataStoreRepository

class CreateViewModel(private val pref: DataStoreRepository): ViewModel() {
    private val apiService = ApiConfig.getApiService()

    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }

    fun uploadImage() {

    }
}