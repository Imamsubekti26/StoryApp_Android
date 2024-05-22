package com.imamsubekti.storyapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.imamsubekti.storyapp.repository.DataStoreRepository

class ViewModelFactory (private val pref: DataStoreRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(pref) as T
        } else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return LoginViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}