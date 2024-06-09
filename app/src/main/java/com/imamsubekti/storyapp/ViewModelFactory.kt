package com.imamsubekti.storyapp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.imamsubekti.storyapp.network.ApiConfig
import com.imamsubekti.storyapp.network.ApiService
import com.imamsubekti.storyapp.repository.DataStoreRepository
import com.imamsubekti.storyapp.repository.StoryRepository
import com.imamsubekti.storyapp.repository.dataStore
import com.imamsubekti.storyapp.view.create.CreateViewModel
import com.imamsubekti.storyapp.view.detail.DetailViewModel
import com.imamsubekti.storyapp.view.list.ListViewModel
import com.imamsubekti.storyapp.view.login.LoginViewModel
import com.imamsubekti.storyapp.view.maps.MapsViewModel
import com.imamsubekti.storyapp.view.splash.SplashViewModel

class ViewModelFactory (private val pref: DataStoreRepository, private val apiService: ApiService) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> SplashViewModel(pref) as T
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(pref, apiService) as T
            modelClass.isAssignableFrom(ListViewModel::class.java) -> ListViewModel(pref, apiService, StoryRepository()) as T
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> MapsViewModel(pref, apiService) as T
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> DetailViewModel(pref, apiService) as T
            modelClass.isAssignableFrom(CreateViewModel::class.java) -> CreateViewModel(pref, apiService) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                val pref = DataStoreRepository.getInstance(context.dataStore)
                val apiService = ApiConfig.getApiService()
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(pref, apiService)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}