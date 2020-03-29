package com.example.photofinder.di.modules.activity

import androidx.lifecycle.ViewModel
import com.example.photofinder.di.mapkey.ViewModelKey
import com.example.photofinder.di.scopes.PerActivityScope
import com.example.photofinder.ui.home.HomeViewModel
import com.example.photofinder.ui.home.ImageAdapter
import com.squareup.picasso.Picasso
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

//Add whatever is needed in home activity either view model or the adapter or any fragment module if required ,etc here

@Module
abstract class HomeActivityModule {

    companion object{

        @Provides
        @PerActivityScope
        fun imageAdapter(picasso: Picasso): ImageAdapter {
            return ImageAdapter(picasso = picasso)
        }
    }

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(homeViewModel: HomeViewModel): ViewModel
}