package com.example.mylocationapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class LoginActivity : AppCompatActivity() {

    lateinit var userNameET : EditText
    lateinit var mobileNumET : EditText
    lateinit var loginBtn : Button

    private var userName : String ?= null
    private var mobileNumber : String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        userNameET = findViewById(R.id.userNameET)
        mobileNumET = findViewById(R.id.mobileNumET)
        loginBtn = findViewById(R.id.loginBtn)

        loginBtn.setOnClickListener {

            userName = userNameET.text.toString()
            mobileNumber = mobileNumET.text.toString()

            if (userName!!.length < 1 || mobileNumber!!.length != 10 )
            {
                Toast.makeText(applicationContext, "Invalid Fields !" , Toast.LENGTH_SHORT).show()
            }
            else {
                val preference = getSharedPreferences(resources.getString(R.string.app_name), Context.MODE_PRIVATE)
                val editor = preference.edit()
                editor.putBoolean(resources.getString(R.string.LOGGED_IN), true)
                editor.putString(resources.getString(R.string.USER_NAME), userName)
                editor.putString(resources.getString(R.string.MOBILE_NUMBER), mobileNumber)
                editor.apply()

                login()
            }
        }
    }

    private fun login() {
        Toast.makeText(applicationContext, "Login Successful !" , Toast.LENGTH_SHORT).show()

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(resources.getString(R.string.USER_NAME), userName)
        startActivity(intent)
        finish()
    }
}