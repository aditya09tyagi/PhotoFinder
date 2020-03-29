@file:Suppress("unused")

package com.example.photofinder.di.modules.network

import com.example.photofinder.data.network.PhotoFinderService
import com.example.photofinder.di.scopes.PhotoFinderApplicationScope
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module(includes = [NetworkModule::class])
class PhotoFinderServiceModule {

    companion object {
        private const val BASE_URL = "https://api.flickr.com/services/rest/"
    }

    @Provides
    @PhotoFinderApplicationScope
    fun setUpService(retrofit: Retrofit): PhotoFinderService {
        return retrofit.create(PhotoFinderService::class.java)
    }

    @Provides
    @PhotoFinderApplicationScope
    fun retrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .build()
    }

    @Provides
    @PhotoFinderApplicationScope
    fun gson(): Gson {
        return GsonBuilder().create()
    }
}