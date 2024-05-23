package com.imamsubekti.storyapp.view.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.imamsubekti.storyapp.repository.DataStoreRepository

class SplashViewModel(private val pref: DataStoreRepository): ViewModel() {
    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }
}