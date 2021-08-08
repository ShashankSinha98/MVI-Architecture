package com.example.mviarchitecture.api

import androidx.lifecycle.LiveData
import com.example.mviarchitecture.model.BlogPost
import com.example.mviarchitecture.model.User
import com.example.mviarchitecture.util.GenericApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("placeholder/blogs")
    fun getBlogPosts(): LiveData<GenericApiResponse<List<BlogPost>>>


    @GET("placeholder/user/{userId}")
    fun getUser(@Path("userId") userId: String): LiveData<GenericApiResponse<User>>
}