package com.example.mviarchitecture.ui.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mviarchitecture.R
import com.example.mviarchitecture.model.BlogPost
import com.example.mviarchitecture.ui.DataStateListener
import com.example.mviarchitecture.ui.main.state.MainStateEvent
import com.example.mviarchitecture.ui.main.state.MainStateEvent.*
import com.example.mviarchitecture.ui.main.state.MainViewState
import com.example.mviarchitecture.util.DataState
import com.example.mviarchitecture.util.TopSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_main.*
import java.lang.ClassCastException
import java.lang.Exception

class MainFragment: Fragment(), BlogListAdapter.Interaction {

    lateinit var viewModel: MainViewModel

    private lateinit var dataStateListener: DataStateListener

    private lateinit var blogListAdapter: BlogListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        viewModel = activity?.run {
            ViewModelProvider(this).get(MainViewModel::class.java)
        }?: throw Exception("Invalid Activity")

        subscribeObservers()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)

            val topSpacingItemDecoration = TopSpacingItemDecoration(30)
            addItemDecoration(topSpacingItemDecoration)

            blogListAdapter = BlogListAdapter(this@MainFragment)
            adapter = blogListAdapter
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            dataStateListener = context as DataStateListener
        } catch (e: ClassCastException) {
            println("DEBUG: $context must implement DataStateListener")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            R.id.action_get_user -> triggerGetUserEvent()

            R.id.action_get_blogs -> triggerGetBlogsEvent()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun triggerGetBlogsEvent() {
        viewModel.setStateEvent(GetBlogPostsEvent())
    }

    private fun triggerGetUserEvent() {
        viewModel.setStateEvent(GetUserEvent("1"))
    }

    private fun subscribeObservers() {


        viewModel.dataState.observe(viewLifecycleOwner,  { dataState ->

            println("DEBUG: DataState: $dataState")

            // handle message(error), Loading
            dataStateListener.onDataStateChanged(dataState)


            // Handle Data<T>
            dataState.data?.let { event ->

                event.getContentIfNotHandled()?.let {  mainViewState ->

                    mainViewState.blogPosts?.let {
                        // set blog posts
                        viewModel.setBlogListData(it)
                    }

                    mainViewState.user?.let {
                        // set user data
                        viewModel.setUserData(it)
                    }
                }
            }

        })

        // java.lang.IllegalArgumentException: Cannot add the same observer with different lifecycles
        // Getting above error using normal way (lambda exp)
        // See link for more info - https://julien-bouffard.medium.com/beware-of-observer-lambdas-with-android-livedata-b27ae935b420

        viewModel.viewState.observe(viewLifecycleOwner, object: Observer<MainViewState> {
            override fun onChanged(viewState: MainViewState?) {

                viewState?.blogPosts?.let {
                    // set BlogPosts to RecyclerView
                    println("DEBUG: Setting blog posts to RecyclerView: ${viewState.blogPosts}")
                    blogListAdapter.submitList(it)
                }

                viewState?.user?.let{
                    // set User data to widgets
                    println("DEBUG: Setting User data: ${viewState.user}")
                }
            }

        })


    }

    override fun onItemSelected(position: Int, item: BlogPost) {
        println("DEBUG: Position: $position, Item: $item")
    }
}