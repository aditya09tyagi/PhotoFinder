package com.example.photofinder.di.modules.activity

import com.example.photofinder.di.modules.fragment.BaseFragmentModule
import com.example.photofinder.di.scopes.PerActivityScope
import com.example.photofinder.ui.home.HomeActivity
import com.example.photofinder.ui.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [BaseFragmentModule::class])
abstract class ActivityBindingModule{

    //Add module of the activities here if the activity is dependent on any constraint

    @PerActivityScope
    @ContributesAndroidInjector(modules = [HomeActivityModule::class])
    internal abstract fun homeActivity():HomeActivity

    @PerActivityScope
    @ContributesAndroidInjector()
    internal abstract fun splashActivity():SplashActivity

}