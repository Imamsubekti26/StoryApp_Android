package com.imamsubekti.storyapp.utils

import androidx.recyclerview.widget.DiffUtil
import com.imamsubekti.storyapp.entity.Story

class StoryDiffCallback : DiffUtil.ItemCallback<Story>() {
    override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
        return oldItem == newItem
    }
}