package com.imamsubekti.storyapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.imamsubekti.storyapp.databinding.ActivityListBinding
import com.imamsubekti.storyapp.repository.DataStoreRepository
import com.imamsubekti.storyapp.repository.dataStore
import com.imamsubekti.storyapp.ui.adaptor.StoryListAdapter
import com.imamsubekti.storyapp.viewmodel.ListViewModel
import com.imamsubekti.storyapp.viewmodel.ViewModelFactory

class ListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListBinding
    private lateinit var model: ListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = DataStoreRepository.getInstance(application.dataStore)
        model = ViewModelProvider(this, ViewModelFactory(pref))[ListViewModel::class.java]

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager

        model.getToken().observe(this){
            model.updateList(it)
        }

        model.listStory.observe(this){
            binding.rvUser.adapter = StoryListAdapter(it.listStory)
        }
    }
}