package com.example.mviarchitecture.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(


    @Expose
    @SerializedName("image")
    val image: String?= null,

    @Expose
    @SerializedName("username")
    val username: String?= null,

    @Expose
    @SerializedName("email")
    val email: String?= null
) {

    override fun toString(): String {
        return "User(image='$image', username='$username', email='$email')"
    }
}