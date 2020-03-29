package com.example.photofinder.data.models

import com.google.gson.annotations.SerializedName

data class ResponseImages(
    @SerializedName("photos")
    val photos: Photos,
    @SerializedName("stat")
    val status: String
)