package com.example.photofinder.di.modules.helper

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.photofinder.di.modules.network.NetworkModule
import com.example.photofinder.di.scopes.PhotoFinderApplicationScope
import com.example.photofinder.util.SharedPreferenceUtil
import com.google.gson.Gson
import dagger.Module
import dagger.Provides

@Module(includes = [NetworkModule::class])
object SharedPreferenceModule {

    @Provides
    fun sharedPreferenceUtil(preferences: SharedPreferences, gson: Gson): SharedPreferenceUtil {
        return SharedPreferenceUtil(preferences, gson)
    }

    @Provides
    @PhotoFinderApplicationScope
    fun preferences(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

}