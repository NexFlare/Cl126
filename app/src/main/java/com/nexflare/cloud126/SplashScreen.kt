package com.nexflare.cloud126

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : AppCompatActivity() {

    lateinit var firebaseAuth: FirebaseAuth
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        sharedPreferences=this.getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
        setContentView(R.layout.activity_splash_screen)
        val decorView = window.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
        decorView.systemUiVisibility = uiOptions
        /*val actionBar = actionBar
        actionBar!!.hide()*/
        Handler().postDelayed({

                if(firebaseAuth.currentUser==null) {
                    startActivity(Intent(this@SplashScreen, MainActivity::class.java))
                    finish()
                }
                else{
                    startActivity(Intent(this@SplashScreen, BrowseActivity::class.java))
                    finish()
                }

        },2000)
    }
}
