package com.example.mahindraassignmentapp.data.model

import com.google.gson.annotations.SerializedName

data class Notification(


    @SerializedName("userId")
    val userId: Int = 0,

    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("title")
    val title: String = "",

    @SerializedName("body")
    val body: String = "",

    var tag:String ="info",

    val time: String = "2 min ago"

)
