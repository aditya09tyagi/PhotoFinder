package com.example.photofinder

import android.app.Activity
import android.app.Service
import android.view.Gravity
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.photofinder.data.event.NoInternetEvent
import com.example.photofinder.di.components.DaggerPhotoFinderApplicationComponent
import com.example.photofinder.di.components.PhotoFinderApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import es.dmoral.toasty.Toasty
import io.github.inflationx.viewpump.ViewPump
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber
import javax.inject.Inject

class PhotoFinderApp : DaggerApplication(), LifecycleObserver, HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    private lateinit var component: PhotoFinderApplicationComponent

    private lateinit var toast: Toast

    companion object {
        const val defaultOffset = 0
        var isAppInForeground = false

        fun get(activity: Activity): PhotoFinderApp {
            return activity.application as PhotoFinderApp
        }

        fun get(service: Service): PhotoFinderApp {
            return service.application as PhotoFinderApp
        }
    }

    override fun onCreate() {
        super.onCreate()
        addProcessObserver()
        initComponent()
        initTimber()
        initViewPump()
        listenNoInternetEvent()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        if (!::component.isInitialized)
            initComponent()
        return component
    }

    private fun addProcessObserver() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    private fun initTimber() {
        Timber.plant(component.timberTree())
    }

    private fun initViewPump() {
        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(component.calligraphyInterceptor())
                .build()
        )
    }

    private fun initComponent() {
        component = DaggerPhotoFinderApplicationComponent.factory().create(applicationContext)
        component.inject(this)
    }

    private fun listenNoInternetEvent() {
        EventBus.getDefault().register(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppGoBackground() {
        isAppInForeground = false
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppComeForeground() {
        isAppInForeground = true
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNoInternetEvent(noInternetEvent: NoInternetEvent) {
        showToast()
    }

    private fun showToast() {
        if (!::toast.isInitialized) {
            toast = Toasty.error(this, getString(R.string.no_internet), Toasty.LENGTH_LONG)
            toast.setGravity(Gravity.CENTER, defaultOffset, defaultOffset)
        }
        toast.show()
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }
}