package com.imamsubekti.storyapp.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.imamsubekti.storyapp.entity.Story
import com.imamsubekti.storyapp.network.ApiService
import com.imamsubekti.storyapp.network.StoryPagingSource

class StoryRepository {
    fun getStory(apiService: ApiService, authorization: String): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, authorization)
            }
        ).liveData
    }
}