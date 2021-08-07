package com.example.mviarchitecture.ui.main.state

sealed class MainStateEvent {

    class GetBlogPostsEvent: MainStateEvent()

    class GetUserEvent(val userID: String): MainStateEvent()

    class None: MainStateEvent()
}