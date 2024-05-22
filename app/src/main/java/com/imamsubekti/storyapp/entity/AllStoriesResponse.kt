package com.imamsubekti.storyapp.entity

import com.google.gson.annotations.SerializedName

data class AllStoriesResponse(

	@field:SerializedName("listStory")
	val listStory: List<Story?>? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
