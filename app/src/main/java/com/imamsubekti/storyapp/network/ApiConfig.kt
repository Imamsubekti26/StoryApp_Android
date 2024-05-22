package com.imamsubekti.storyapp.network

import com.imamsubekti.storyapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        fun getApiService(withBearer: Boolean? = false): ApiService {
            val client = OkHttpClient.Builder().run {
                addInterceptor { chain ->
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

                    val request = chain
                        .request()
                        .newBuilder().run {
                            if (withBearer as Boolean) addHeader("Authorization", "Bearer <token>")
                            build()
                        }

                    chain.proceed(request)

                }
                build()
            }

            val retrofit = Retrofit.Builder().run {
                baseUrl(BuildConfig.BASE_URL)
                addConverterFactory(GsonConverterFactory.create())
                client(client)
                build()
            }

            return retrofit.create(ApiService::class.java)
        }
    }
}