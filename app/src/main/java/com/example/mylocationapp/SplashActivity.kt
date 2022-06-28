package com.example.mylocationapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            initActivity()

        }, 3000)
    }

    private fun initActivity() {
        try {
            val preference=getSharedPreferences(resources.getString(R.string.app_name), Context.MODE_PRIVATE)
            val isLoggedIn = preference.getBoolean(resources.getString(R.string.LOGGED_IN),false)
            val userName = preference.getString(resources.getString(R.string.USER_NAME), "User")

            if (isLoggedIn){
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra(resources.getString(R.string.USER_NAME), userName)
                startActivity(intent)
                finish()
            }
            else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

        }catch (e: Exception){
            Log.d("SplashActivity", " Empty Shared Preference ")
            e.stackTrace
        }
    }
}