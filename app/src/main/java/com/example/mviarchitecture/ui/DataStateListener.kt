package com.example.mviarchitecture.ui

import com.example.mviarchitecture.util.DataState

interface DataStateListener {

    fun onDataStateChanged(dataState: DataState<*>?) // Type Invariant
}