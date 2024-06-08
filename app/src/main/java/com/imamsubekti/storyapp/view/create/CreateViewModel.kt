package com.imamsubekti.storyapp.view.create

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.imamsubekti.storyapp.entity.BasicResponse
import com.imamsubekti.storyapp.network.ApiConfig
import com.imamsubekti.storyapp.repository.DataStoreRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateViewModel(private val pref: DataStoreRepository): ViewModel() {
    private val apiService = ApiConfig.getApiService()

    private val _currentImageUri = MutableLiveData<Uri>()
    val currentImageUri: LiveData<Uri> get() = _currentImageUri
    fun setCurrentImageUri(uri: Uri) = _currentImageUri.postValue(uri)

    private val _status = MutableLiveData<CreateStatus>()
    val status: LiveData<CreateStatus> get() = _status

    fun getToken(): LiveData<String> = pref.getToken().asLiveData()

    private val _location = MutableLiveData<Location>()

    fun setLocation(lon: Double, lat: Double) {
        _location.postValue(
            Location(
                latitude = lat,
                longitude = lon
            )
        )
    }

    fun uploadNewStory(
        token: String,
        description: String,
        photo: MultipartBody.Part
    ){
        _status.postValue(CreateStatus.PROGRESS)

        val desc = description.toRequestBody("text/plain".toMediaTypeOrNull())
        val longitude = if (_location.value?.longitude == 0.0) null else _location.value?.longitude
        val latitude = if (_location.value?.latitude == 0.0) null else _location.value?.latitude

        apiService.createStory("Bearer $token", desc, photo, latitude, longitude).enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if (response.isSuccessful && !response.body()?.error!!) {
                    _status.postValue(CreateStatus.SUCCESS)
                } else {
                    _status.postValue(CreateStatus.FAILED)
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                _status.postValue(CreateStatus.FAILED)
            }

        })
    }

    enum class CreateStatus(val value: Int) {
        SUCCESS(200),
        FAILED(400),
        PROGRESS(100)
    }

    data class Location(
        val latitude: Double,
        val longitude: Double
    )
}