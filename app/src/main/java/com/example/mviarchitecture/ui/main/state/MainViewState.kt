package com.example.mviarchitecture.ui.main.state

import com.example.mviarchitecture.model.BlogPost
import com.example.mviarchitecture.model.User

data class MainViewState(

    var user: User?= null,

    var blogPosts: List<BlogPost>?= null
)