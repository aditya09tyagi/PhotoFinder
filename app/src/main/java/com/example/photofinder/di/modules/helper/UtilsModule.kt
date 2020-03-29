package com.example.photofinder.di.modules.helper

import android.content.Context
import com.example.photofinder.di.modules.libraries.PicassoModule
import com.example.photofinder.di.scopes.PhotoFinderApplicationScope
import com.example.photofinder.util.*
import dagger.Module
import dagger.Provides

@Module(includes = [ContextModule::class, PicassoModule::class])
class UtilsModule {

    @Provides
    @PhotoFinderApplicationScope
    fun networkUtil(context: Context): NetworkUtil {
        return NetworkUtil(context)
    }

}