package com.example.mviarchitecture.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.mviarchitecture.model.BlogPost
import com.example.mviarchitecture.model.User
import com.example.mviarchitecture.repository.Repository
import com.example.mviarchitecture.ui.main.state.MainStateEvent
import com.example.mviarchitecture.ui.main.state.MainStateEvent.*
import com.example.mviarchitecture.ui.main.state.MainViewState
import com.example.mviarchitecture.util.AbsentLiveData

class MainViewModel: ViewModel() {

    private val TAG = "MainViewModel"

    private val _stateEvent: MutableLiveData<MainStateEvent> = MutableLiveData()
    private val _viewState: MutableLiveData<MainViewState> = MutableLiveData()

    val viewState: LiveData<MainViewState>
        get() = _viewState

    val dataState: LiveData<MainViewState> = Transformations
        .switchMap(_stateEvent) { stateEvent ->
            stateEvent?.let {
                handleStateEvent(stateEvent)
            }
        }

    // fetch data from Repo
    private fun handleStateEvent(stateEvent: MainStateEvent): LiveData<MainViewState> {

        return when(stateEvent) {

            is GetBlogPostsEvent ->  {
                Repository.getBlogPosts()
            }

            is GetUserEvent -> {
                Repository.getUser(stateEvent.userID)
            }

            is None -> AbsentLiveData.create()
        }
    }

    private fun getCurrentViewStateOrNew(): MainViewState {
        return _viewState.value?: MainViewState()
    }

    fun setBlogListData(blogPosts: List<BlogPost>) {
        val update = getCurrentViewStateOrNew()
        update.blogPosts = blogPosts
        _viewState.value = update
    }

    fun setUserData(user: User) {
        val update = getCurrentViewStateOrNew()
        update.user = user
        _viewState.value = update
    }

    fun setStateEvent(event: MainStateEvent) {
        _stateEvent.value = event
    }


}