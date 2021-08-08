package com.example.mviarchitecture.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.mviarchitecture.api.RetrofitBuilder
import com.example.mviarchitecture.ui.main.state.MainViewState
import com.example.mviarchitecture.util.ApiEmptyResponse
import com.example.mviarchitecture.util.ApiErrorResponse
import com.example.mviarchitecture.util.ApiSuccessResponse
import com.example.mviarchitecture.util.DataState

object Repository {

    fun getBlogPosts(): LiveData<DataState<MainViewState>> {
        return Transformations
            .switchMap(RetrofitBuilder.apiService.getBlogPosts()) { apiResponse -> // GenericApiResponse type

                object: LiveData<DataState<MainViewState>>() {
                    override fun onActive() {
                        super.onActive()

                        when(apiResponse) {

                            is ApiSuccessResponse -> {
                                value = DataState.data(
                                    message = null,
                                    data= MainViewState(blogPosts = apiResponse.body)
                                )
                            }

                            is ApiErrorResponse -> {
                                value = DataState.error(
                                    message = apiResponse.errorMessage
                                )
                            }

                            is ApiEmptyResponse -> {
                                value = DataState.error(
                                    message = "HTTP 204. Returned NOTHING!"
                                )
                            }
                        }
                    }
                }
            }
    }



    fun getUser(userId: String): LiveData<DataState<MainViewState>> {
        return Transformations
            .switchMap(RetrofitBuilder.apiService.getUser(userId)) { apiResponse -> // generic api response
                object : LiveData<DataState<MainViewState>>() {
                    override fun onActive() {
                        super.onActive()

                        when(apiResponse) {

                            is ApiSuccessResponse -> {
                                value = DataState.data(
                                    message = null,
                                    data = MainViewState(user = apiResponse.body)
                                )
                            }

                            is ApiErrorResponse -> {
                                value = DataState.error(
                                    message = apiResponse.errorMessage
                                )
                            }

                            is ApiEmptyResponse -> {
                                value = DataState.error(
                                    message = "HTTP 204. Returned NOTHING!"
                                )
                            }

                        }
                    }
                }
            }
    }

}