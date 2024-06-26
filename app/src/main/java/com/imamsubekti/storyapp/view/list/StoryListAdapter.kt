package com.imamsubekti.storyapp.view.list

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.imamsubekti.storyapp.databinding.StoryCardBinding
import com.imamsubekti.storyapp.entity.Story
import com.imamsubekti.storyapp.view.detail.DetailActivity

class StoryListAdapter: PagingDataAdapter<Story, StoryListAdapter.StoryViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = StoryCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null){
            holder.bind(holder.itemView.context, data)
        }
    }

    inner class StoryViewHolder (private val binding: StoryCardBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.storyCard.setOnClickListener {
                val context = binding.storyCard.context
                val toDetail = Intent(context, DetailActivity::class.java).apply {
                    putExtra("id_story", binding.storyId.text.toString())
                }
                context.startActivity(toDetail)
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(context: Context, story: Story) {
            binding.storyId.text = story.id
            binding.imageOwner.text = "by: ${story.name}"
            Glide.with(context).load(story.photoUrl).into(binding.storyImage)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}