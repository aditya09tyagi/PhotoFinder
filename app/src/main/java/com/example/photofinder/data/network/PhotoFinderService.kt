package com.example.photofinder.data.network

import com.example.photofinder.data.models.ResponseImages
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotoFinderService {
    companion object {
        private const val API_KEY = "9bd21efd4336ba7607b0cbe0898f12a0"
        private const val SECRET = "aeeabf875dc88c48"
        private const val API_METHOD_SEARCH = "flickr.photos.search"
    }

    @GET("?format=json&nojsoncallback=1&method=${API_METHOD_SEARCH}&api_key=${API_KEY}")
    fun getImagesBySearch(
        @Query("text") queryText: String,
        @Query("page") pageNumber: Int
    ): Call<ResponseImages>
}