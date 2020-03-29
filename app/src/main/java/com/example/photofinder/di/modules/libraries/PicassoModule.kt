package com.example.photofinder.di.modules.libraries

import android.content.Context
import com.example.photofinder.di.modules.helper.ContextModule
import com.example.photofinder.di.scopes.PhotoFinderApplicationScope
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides

@Module(includes = [ContextModule::class])
object PicassoModule {

    @Provides
    @PhotoFinderApplicationScope
    fun picasso(context: Context): Picasso {
        return Picasso.Builder(context)
                .build()
    }
}