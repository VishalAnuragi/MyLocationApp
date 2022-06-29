package com.example.mylocationapp


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val timeDelay = 1000L

    private val LOCATION_PERMISSION_REQ_CODE = 1000;
    var gpsStatus: Boolean ?= false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        askPermission()
    }

    private fun askPermission() {
        if ( Build.VERSION.SDK_INT >= 23){
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED ||  ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED){

                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION , Manifest.permission.WRITE_EXTERNAL_STORAGE), LOCATION_PERMISSION_REQ_CODE);
            }
        }
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

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQ_CODE -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted

                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)





                } else {
                    // permission denied
                    Toast.makeText(this, "You need to grant permission to access location",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        Handler().postDelayed({
            if (gpsStatus!!){
                initActivity()
            }
        }, timeDelay)
    }
}