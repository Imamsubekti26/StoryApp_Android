package com.imamsubekti.storyapp.network

import com.imamsubekti.storyapp.entity.AllStoriesResponse
import com.imamsubekti.storyapp.entity.BasicResponse
import com.imamsubekti.storyapp.entity.DetailStoryResponse
import com.imamsubekti.storyapp.entity.LoginRequest
import com.imamsubekti.storyapp.entity.LoginResponse
import com.imamsubekti.storyapp.entity.RegisterRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
        @Part("description") description: RequestBody,
        @Part photo: MultipartBody.Part,
        @Part("lat") latitude: Double? = null,
        @Part("lon") longitude: Double? = null
    ): Call<BasicResponse>

    @GET("stories")
    fun getStories(
        @Header("Authorization") authorization: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("location") location: Int? = 0
    ): Call<AllStoriesResponse>

    @GET("stories")
    suspend fun getStoriesAsync(
        @Header("Authorization") authorization: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("location") location: Int? = 0
    ): AllStoriesResponse

    @GET("stories/{id}")
    fun getStoryById(
        @Header("Authorization") authorization: String,
        @Path("id") storyId: String
    ): Call<DetailStoryResponse>
}
