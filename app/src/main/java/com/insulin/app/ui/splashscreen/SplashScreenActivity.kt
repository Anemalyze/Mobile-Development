package com.insulin.app.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.insulin.app.ui.home.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* init splashscreen API */
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { true }

        /* disable dark mode*/
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        /* force full screen for splash screen */
        @Suppress("DEPRECATION")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

//        val auth = Firebase.auth

        //if (auth.currentUser != null) {
            /* user was login */
        //     startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
        //} else {
        //    startActivity(Intent(this@SplashScreenActivity, OnBoardingActivity::class.java))
        //}
        //finish()
        startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
        finish()
    }
}