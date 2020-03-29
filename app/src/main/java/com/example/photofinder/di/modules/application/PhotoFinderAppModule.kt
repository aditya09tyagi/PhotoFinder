package com.example.photofinder.di.modules.application

import com.example.photofinder.R
import com.example.photofinder.di.scopes.PhotoFinderApplicationScope
import dagger.Module
import dagger.Provides
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import timber.log.Timber

@Module
object PhotoFinderAppModule {

    @Provides
    @PhotoFinderApplicationScope
    fun timberTree(): Timber.Tree {
        return Timber.DebugTree()
    }

    @Provides
    @PhotoFinderApplicationScope
    fun calligraphyInterceptor(): CalligraphyInterceptor {
        return CalligraphyInterceptor(
                CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Lato.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        )
    }
}