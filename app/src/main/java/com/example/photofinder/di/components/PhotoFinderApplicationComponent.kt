package com.example.photofinder.di.components

import android.content.Context
import com.example.photofinder.PhotoFinderApp
import com.example.photofinder.data.network.PhotoFinderRepository
import com.example.photofinder.di.modules.activity.ActivityBindingModule
import com.example.photofinder.di.modules.application.PhotoFinderAppModule
import com.example.photofinder.di.modules.helper.SharedPreferenceModule
import com.example.photofinder.di.modules.helper.UtilsModule
import com.example.photofinder.di.modules.helper.ViewModelFactoryModule
import com.example.photofinder.di.modules.libraries.PicassoModule
import com.example.photofinder.di.modules.network.PhotoFinderRepositoryModule
import com.example.photofinder.di.scopes.PhotoFinderApplicationScope
import com.example.photofinder.util.*
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import timber.log.Timber

@PhotoFinderApplicationScope
@Component(
    modules = [
        PhotoFinderAppModule::class,
        PhotoFinderRepositoryModule::class,
        PicassoModule::class,
        SharedPreferenceModule::class,
        UtilsModule::class,
        ViewModelFactoryModule::class,
        ActivityBindingModule::class,
        AndroidInjectionModule::class
    ]
)
interface PhotoFinderApplicationComponent : AndroidInjector<PhotoFinderApp> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): PhotoFinderApplicationComponent
    }

    fun calligraphyInterceptor(): CalligraphyInterceptor

    fun timberTree(): Timber.Tree

    fun getPicasso(): Picasso

    fun getSetUpRepository(): PhotoFinderRepository

    fun getSharedPreferenceUtil(): SharedPreferenceUtil

    fun getGson(): Gson

}