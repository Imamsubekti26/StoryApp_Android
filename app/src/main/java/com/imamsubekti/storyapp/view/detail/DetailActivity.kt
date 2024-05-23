package com.imamsubekti.storyapp.view.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.imamsubekti.storyapp.databinding.ActivityDetailBinding
import com.imamsubekti.storyapp.repository.DataStoreRepository
import com.imamsubekti.storyapp.repository.dataStore
import com.imamsubekti.storyapp.config.ViewModelFactory

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var model: DetailViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = DataStoreRepository.getInstance(application.dataStore)
        model = ViewModelProvider(this, ViewModelFactory(pref) )[DetailViewModel::class.java]

        val storyId = intent.getStringExtra("id_story")

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        model.getToken().observe(this){
            model.updateDetail(it, storyId as String)
        }

        model.storyDetail.observe(this){
            binding.imageOwner.text = it.story?.name ?: "owner not found"
            binding.imageDescription.text = it.story?.description ?: "no description"
            Glide.with(this).load(it.story?.photoUrl).into(binding.storyImage)
        }
    }
}