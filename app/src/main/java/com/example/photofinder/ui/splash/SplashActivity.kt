package com.example.photofinder.ui.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.photofinder.R
import com.example.photofinder.ui.base.BaseActivity
import com.example.photofinder.ui.home.HomeActivity

class SplashActivity : BaseActivity() {

    companion object{
        fun newIntent(context: Context) = Intent(context,SplashActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        goToHome()
    }

    private fun goToHome() {
        startActivity(HomeActivity.newIntent(this@SplashActivity))
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        finishAffinity()
    }
}