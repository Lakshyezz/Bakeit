package com.example.bakeit

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.bakeit.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_splash_screen)


    val splashBinding: ActivitySplashScreenBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
    setContentView(splashBinding.root)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }

        val splashAnimation = AnimationUtils.loadAnimation(this,R.anim.anime_splash)
    splashBinding.tvAppName.animation = splashAnimation
        splashAnimation.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {
                //
            }

            override fun onAnimationEnd(animation: Animation?) {
                // after animations ends
                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(Intent(this@SplashScreen, MainActivity::class.java))
                    finish()
                },1000)
            }

            override fun onAnimationRepeat(animation: Animation?) {
                TODO("Not yet implemented")
            }
        })
    }
}