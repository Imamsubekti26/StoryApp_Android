package com.imamsubekti.storyapp.view.list

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.imamsubekti.storyapp.R
import com.imamsubekti.storyapp.databinding.ActivityListBinding
import com.imamsubekti.storyapp.repository.DataStoreRepository
import com.imamsubekti.storyapp.repository.dataStore
import com.imamsubekti.storyapp.view.welcome.WelcomeActivity
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

        setupToolBar()

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager

        model.getToken().observe(this){
            if (it.isNotEmpty()){
                model.updateList(it)
            }
        }

        model.listStory.observe(this){
            binding.rvUser.adapter = StoryListAdapter(it.listStory)
        }
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
                else -> false
            }

        }
    }
}