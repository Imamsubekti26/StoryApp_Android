package com.imamsubekti.storyapp.view.list

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.imamsubekti.storyapp.R
import com.imamsubekti.storyapp.databinding.ActivityListBinding
import com.imamsubekti.storyapp.view.welcome.WelcomeActivity
import com.imamsubekti.storyapp.ViewModelFactory
import com.imamsubekti.storyapp.view.create.CreateActivity
import com.imamsubekti.storyapp.view.maps.MapsActivity

class ListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListBinding
    private lateinit var model: ListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model = ViewModelProvider(this, ViewModelFactory.getInstance(this))[ListViewModel::class.java]

        setupToolBar()
        getStoryList()
        setupRecycleView()
    }

    private fun setupToolBar() {
        binding.toolbar.inflateMenu(R.menu.main_menu)
        binding.toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.logout_button -> {
                    model.removeToken()
                    val toWelcome = Intent(this, WelcomeActivity::class.java)
                    toWelcome.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(toWelcome)
                    finish()
                    true
                }
                R.id.gmaps_button -> {
                    val toMaps = Intent(this, MapsActivity::class.java)
                    startActivity(toMaps)
                    true
                }
                R.id.add_button -> {
                    val toAdd = Intent(this, CreateActivity::class.java)
                    startActivity(toAdd)
                    true
                }
                else -> false
            }
        }
    }

    private fun getStoryList(){
        model.getToken().observe(this){
            if (it.isNotEmpty()){
                model.updateList(it)
            }
        }
    }

    private fun setupRecycleView(){
        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager

        val adapter = StoryListAdapter()
        binding.rvUser.adapter = adapter

        model.listStory.observe(this){
            adapter.submitData(lifecycle, it)
        }
    }
}