package com.example.mviarchitecture.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.mviarchitecture.api.RetrofitBuilder
import com.example.mviarchitecture.ui.main.state.MainViewState
import com.example.mviarchitecture.util.ApiEmptyResponse
import com.example.mviarchitecture.util.ApiErrorResponse
import com.example.mviarchitecture.util.ApiSuccessResponse

object Repository {

    fun getBlogPosts(): LiveData<MainViewState> {
        return Transformations
            .switchMap(RetrofitBuilder.apiService.getBlogPosts()) { apiResponse -> // GenericApiResponse type

                object: LiveData<MainViewState>() {
                    override fun onActive() {
                        super.onActive()

                        when(apiResponse) {

                            is ApiSuccessResponse -> {
                                value = MainViewState(
                                    blogPosts = apiResponse.body
                                )
                            }

                            is ApiErrorResponse -> {
                                value = MainViewState() // handle error?
                            }

                            is ApiEmptyResponse -> {
                                value = MainViewState() // handle empty/error?
                            }
                        }
                    }
                }
            }
    }



    fun getUser(userId: String): LiveData<MainViewState> {
        return Transformations
            .switchMap(RetrofitBuilder.apiService.getUser(userId)) { apiResponse -> // generic api response
                object : LiveData<MainViewState>() {
                    override fun onActive() {
                        super.onActive()

                        when(apiResponse) {

                            is ApiSuccessResponse -> {
                                value = MainViewState(
                                    user = apiResponse.body
                                )
                            }

                            is ApiErrorResponse -> {
                                value = MainViewState() // handle error?
                            }

                            is ApiEmptyResponse -> {
                                value = MainViewState() // handle empty/error?
                            }

                        }
                    }
                }
            }
    }

}