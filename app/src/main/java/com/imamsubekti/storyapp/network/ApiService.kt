package com.imamsubekti.storyapp.network

import com.imamsubekti.storyapp.entity.AllStoriesResponse
import com.imamsubekti.storyapp.entity.BasicResponse
import com.imamsubekti.storyapp.entity.DetailStoryResponse
import com.imamsubekti.storyapp.entity.LoginRequest
import com.imamsubekti.storyapp.entity.LoginResponse
import com.imamsubekti.storyapp.entity.RegisterRequest
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("register")
    fun register(
        @Body registerRequest: RegisterRequest
    ): Call<BasicResponse>

    @POST("login")
    fun login(
        @Body loginRequest: LoginRequest
    ): Call<LoginResponse>

    @Multipart
    @POST("stories")
    fun createStory(
        @Header("Authorization") authorization: String,
        @Part("description") description: String,
        @Part("photo") photo: MultipartBody.Part,
        @Part("lat") latitude: Float,
        @Part("lon") longitude: Float
    ): Call<BasicResponse>

    @GET("stories")
    fun getStories(
        @Header("Authorization") authorization: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("location") location: Location? = Location.DISABLED
    ): Call<AllStoriesResponse>

    @GET("stories/{id}")
    fun getStoryById(
        @Header("Authorization") authorization: String,
        @Path("id") storyId: Int
    ): Call<DetailStoryResponse>
}

enum class Location(val value: Int) {
    DISABLED(0),
    ENABLED(1)
}
