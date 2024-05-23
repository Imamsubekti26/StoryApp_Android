package com.imamsubekti.storyapp.config

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.imamsubekti.storyapp.repository.DataStoreRepository
import com.imamsubekti.storyapp.view.detail.DetailViewModel
import com.imamsubekti.storyapp.view.list.ListViewModel
import com.imamsubekti.storyapp.view.login.LoginViewModel
import com.imamsubekti.storyapp.view.splash.SplashViewModel

class ViewModelFactory (private val pref: DataStoreRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            return SplashViewModel(pref) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(pref) as T
        } else if (modelClass.isAssignableFrom(ListViewModel::class.java)) {
            return ListViewModel(pref) as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}