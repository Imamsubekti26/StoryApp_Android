package com.imamsubekti.storyapp.view.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.imamsubekti.storyapp.entity.Story
import com.imamsubekti.storyapp.network.ApiService
import com.imamsubekti.storyapp.repository.DataStoreRepository
import com.imamsubekti.storyapp.repository.StoryRepository
import kotlinx.coroutines.launch

class ListViewModel(
    private val pref: DataStoreRepository,
    private val apiService: ApiService,
    private val storyRepository: StoryRepository
): ViewModel() {

    private val _listStory = MutableLiveData<PagingData<Story>>()
    val listStory:LiveData<PagingData<Story>> get() = _listStory

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }

    fun removeToken() {
        viewModelScope.launch {
            pref.logout()
        }
    }

    fun updateList(token: String){
        val data = storyRepository.getStory(apiService, "Bearer $token")

        data.cachedIn(viewModelScope).observeForever {
            _listStory.postValue(it)
        }
    }
}